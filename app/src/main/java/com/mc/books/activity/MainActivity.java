package com.mc.books.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.auth0.android.jwt.JWT;
import com.bon.network.NetworkUtils;
import com.bon.permission.PermissionUtils;
import com.bon.sharepreferences.AppPreferences;
import com.bon.viewanimation.Techniques;
import com.bon.viewanimation.YoYo;
import com.mc.application.AppContext;
import com.mc.books.R;
import com.mc.books.fragments.gift.myGift.GiftFragment;
import com.mc.books.fragments.home.dashboad.DashboardFragment;
import com.mc.books.fragments.home.infomationBook.InformationBookFragment;
import com.mc.books.fragments.home.listLesson.ListLessonFragment;
import com.mc.books.fragments.home.listSubject.ListSubjectFragment;
import com.mc.books.fragments.more.tabmore.MoreFragment;
import com.mc.books.fragments.notification.detailnotification.DetailNotificationFragment;
import com.mc.books.fragments.notification.NotificationFragment;
import com.mc.books.services.KillAppService;
import com.mc.common.activities.BaseAppCompatActivity;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.customizes.bottombars.ItemBottomBar;
import com.mc.events.NetworkEvent;
import com.mc.events.NotificationEvent;
import com.mc.models.BaseResponse;
import com.mc.models.User;
import com.mc.models.notification.NotificationResponse;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;
import com.mc.utilities.shadow.ShadowProperty;
import com.onesignal.OneSignal;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.mc.utilities.Constant.KEY_ACCESS_TOKEN;
import static com.mc.utilities.Constant.KEY_COMMON_BOOK_NOTI;
import static com.mc.utilities.Constant.KEY_COURSES_BOOK_NOTI;
import static com.mc.utilities.Constant.KEY_EXERCISES_BOOK_NOTI;
import static com.mc.utilities.Constant.KEY_TYPE_NOTI_MANUAL;
import static com.mc.utilities.Constant.KEY_TYPE_NOTI_MYBOOK;
import static com.mc.utilities.Constant.KEY_USER_ID;
import static com.mc.utilities.Constant.KEY_USER_INFO;

public class MainActivity extends BaseAppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.home)
    ItemBottomBar home;
    @BindView(R.id.gift)
    ItemBottomBar gift;
    @BindView(R.id.notification)
    ItemBottomBar mNotification;
    @BindView(R.id.more)
    ItemBottomBar more;
    @BindView(R.id.llBottomBar)
    LinearLayout llBottomBar;
    @BindView(R.id.tvNoInternet)
    TextView tvNoInternet;
    private int countNoti;
    Intent killAppIntent;
    User user;

    private String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.RECORD_AUDIO};

    public int currentFragmentIndex = Constant.HOME_TAB;

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            EventBus.getDefault().post(new NetworkEvent(NetworkUtils.isNetworkAvailable(MainActivity.this)));
        }
    }

    NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkEvent(NetworkEvent event) {
        tvNoInternet.setVisibility(event.isHaveNetwork() ? View.GONE : View.VISIBLE);
    }

    public void updateStatusNetwork() {
        // show no network
        tvNoInternet.setVisibility(!NetworkUtils.isNetworkAvailable(this)
                ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        try {
            parseUserInfo();
            if (!PermissionUtils.hasPermissions(this, perms)) {
                initPermision();
            }

            AppUtils.setShadow(this, llBottomBar, ShadowProperty.TOP);
            OneSignal.setSubscription(!AppPreferences.getInstance(getAppContext()).getBoolean(Constant.DISABLE_RECEIVE_NOTI));
            OneSignal.enableSound(!AppPreferences.getInstance(getAppContext()).getBoolean(Constant.DISABLE_SOUND_NOTI));
            EventBus.getDefault().register(this);
            //  bindNetworkService();
            OneSignal.idsAvailable((userId, registrationId) -> {
                Log.d("onesignal", "User:" + userId);
                if (registrationId != null)
                    Log.d("onesignal", "registrationId:" + registrationId);
                apiService.pushDeviceId(userId).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()).subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {

                    }
                });

            });
            this.initFragmentDefault();
            processNotification(getIntent());


            // register notification event listener
            registerNotificationEvent();

            // start service detect kill app
            killAppIntent = new Intent(this, KillAppService.class);
            startService(killAppIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void parseUserInfo() {
        JWT jwt = new JWT(AppPreferences.getInstance(this).getString(KEY_ACCESS_TOKEN));
        if (jwt.getSubject() != null)
            AppPreferences.getInstance(this).putString(KEY_USER_ID, jwt.getSubject());

        // get notification
        getCountNoti();

        apiService.getDetailUser(jwt.getSubject()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<User>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("USER error", e.toString());
            }

            @Override
            public void onNext(BaseResponse<User> userBaseResponse) {
                AppPreferences.getInstance(getAppContext()).putJson(KEY_USER_INFO, userBaseResponse.getData());
                user = userBaseResponse.getData();
            }
        });
    }


    private void registerNotificationEvent() {
        bus.subscribe(this, NotificationEvent.class, notificationEvent -> {
            if (notificationEvent.getEvent().equals(Constant.KEY_NOTIFICATION_EVENT)) {
//                countReset++;
                countNoti++;
                if (mNotification == null) return;
                mNotification.setBadgeActive(true);
                mNotification.setCountNotifycation(countNoti);
                ShortcutBadger.applyCount(getAppContext(), countNoti);
//                if (isResetCount) {
//                    mNotification.setBadgeActive(true);
//                    mNotification.setCountNotifycation(countReset);
//                    ShortcutBadger.applyCount(getAppContext(), countReset);
//                } else {
//                    mNotification.setBadgeActive(true);
//                    mNotification.setCountNotifycation(countNoti);
//                    ShortcutBadger.applyCount(getAppContext(), countNoti);
//                }
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processNotification(intent);
    }

    public void processNotification(Intent intent) {
        registerNotificationEvent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String type = bundle.getString("type");
            String action = bundle.getString("action");
            int bookId = bundle.getInt("bookId");
            if (type != null) {
                switch (type) {
                    case KEY_TYPE_NOTI_MYBOOK:
                        if (action != null) {
                            switch (action) {
                                case KEY_COMMON_BOOK_NOTI:
                                    changeTabBottom(Constant.HOME_TAB);
                                    FragmentUtils.replaceFragment(this, InformationBookFragment.newInstance(bookId), frag -> this.fragments.add(frag));
                                    break;

                                case KEY_COURSES_BOOK_NOTI:
                                    changeTabBottom(Constant.HOME_TAB);
                                    FragmentUtils.replaceFragment(this, ListLessonFragment.newInstance(bookId, 0), frag -> this.fragments.add(frag));
                                    break;

                                case KEY_EXERCISES_BOOK_NOTI:
                                    changeTabBottom(Constant.HOME_TAB);
                                    FragmentUtils.replaceFragment(this, ListSubjectFragment.newInstance(bookId), frag -> this.fragments.add(frag));
                                    break;
                            }
                        }
                        break;
                    case KEY_TYPE_NOTI_MANUAL:
                        changeTabBottom(Constant.NOTIFICATION_TAB);
                        FragmentUtils.replaceFragment(this, DetailNotificationFragment.newInstance(bookId), frag -> this.fragments.add(frag));
                        break;
                    case Constant.KEY_TYPE_NOTI_DAYS:
                        changeTabBottom(Constant.HOME_TAB);
                        FragmentUtils.replaceFragment(this, DashboardFragment.newInstance(), frag -> this.fragments.add(frag));
                        break;
                }
            }
        }
    }


    private void initPermision() {
        PermissionUtils.requestPermission(this, PermissionUtils.REQUEST_CODE_PERMISSION, perms);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.REQUEST_CODE_PERMISSION: {
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permision write extenal storage
                    AppContext.setConfigRealm(this);
                    if (user != null)
                        AppUtils.writeToFile(MainActivity.this, "", user.getFirstName());
                }
            }
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.main_activity;
    }

    @Override
    public ActionBar getAppSupportActionBar() {
        return getSupportActionBar();
    }

    @Override
    public void initFragmentDefault() {
        this.changeTabBottom(currentFragmentIndex);
    }

    private void playAnimator(ItemBottomBar view) {
        YoYo.with(Techniques.ReBounceAnimator)
                .duration(1000)
                .playOn(view.getImageView());
    }

    public void onShowBottomBar(boolean isShow) {
        if (isShow)
            llBottomBar.setVisibility(View.VISIBLE);
        else
            llBottomBar.setVisibility(View.GONE);
    }

    private void activeTab(int index) {
        home.setActiveMode(false);
        gift.setActiveMode(false);
        mNotification.setActiveMode(false);
        more.setActiveMode(false);
        home.setBadgeActive(false);
        gift.setBadgeActive(false);
        more.setBadgeActive(false);

        if (index == Constant.HOME_TAB) {
            home.setActiveMode(true);
            home.setBadgeActive(false);
        } else if (index == Constant.GIFT_TAB) {
            gift.setActiveMode(true);
            gift.setBadgeActive(false);
        } else if (index == Constant.NOTIFICATION_TAB) {
            mNotification.setActiveMode(true);
            mNotification.setBadgeActive(false);
            ShortcutBadger.removeCount(MainActivity.this);
//            isResetCount = true;
            countNoti = 0;
            resetCountNoti();
        } else if (index == Constant.MORE_TAB) {
            more.setBadgeActive(false);
            more.setActiveMode(true);
        }
    }

    private void resetCountNoti() {
        apiService.resetBadge().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String s) {
            }
        });
    }

    @OnClick({R.id.home, R.id.gift, R.id.notification, R.id.more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home:
                this.playAnimator(home);
                this.activeTab(Constant.HOME_TAB);
                changeTabBottom(Constant.HOME_TAB);
                break;
            case R.id.gift:
                this.playAnimator(gift);
                this.activeTab(Constant.GIFT_TAB);
                changeTabBottom(Constant.GIFT_TAB);
                break;
            case R.id.notification:
                this.playAnimator(mNotification);
                this.activeTab(Constant.NOTIFICATION_TAB);
                changeTabBottom(Constant.NOTIFICATION_TAB);
                break;
            case R.id.more:
                this.playAnimator(more);
                this.activeTab(Constant.MORE_TAB);
                changeTabBottom(Constant.MORE_TAB);
                break;
        }
    }

    public void changeTabBottom(int index) {
        try {
            BaseMvpFragment fragment;
            switch (index) {
                case Constant.HOME_TAB:
                    fragment = DashboardFragment.newInstance();
                    break;
                case Constant.GIFT_TAB:
                    fragment = GiftFragment.newInstance();
                    break;
                case Constant.NOTIFICATION_TAB:
                    fragment = NotificationFragment.newInstance();
                    break;
                case Constant.MORE_TAB:
                    fragment = MoreFragment.newInstance();
                    break;
                default:
                    fragment = DashboardFragment.newInstance();
            }
            this.activeTab(index);
            FragmentUtils.replaceFragment(this, fragment);
            this.currentFragmentIndex = index;
            this.fragments.clear();
        } catch (Exception e) {
//asd
        }
    }

    private void getCountNoti() {
        Map<String, String> maps = new HashMap<>();
        maps.put("start", String.valueOf(0));
        maps.put("limit", String.valueOf(Constant.LIMIT_PAGING));
        apiService.getNotificationList(maps).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<BaseResponse<NotificationResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("getNotificationList err", e.getMessage());
            }

            @Override
            public void onNext(BaseResponse<NotificationResponse> notificationResponse) {
                countNoti = notificationResponse.getData().getTotalBaggy();
//                isResetCount = false;
                if (countNoti > 0) {
                    mNotification.setBadgeActive(true);
                    mNotification.setCountNotifycation(countNoti);
                } else {
                    mNotification.setBadgeActive(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        updateStatusNetwork();
        registerNotificationEvent();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(networkChangeReceiver);
        EventBus.getDefault().unregister(this);
        stopService(killAppIntent);
        super.onDestroy();
    }
}
