package com.mc.application;

import android.Manifest;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bon.application.ExtApplication;
import com.bon.eventbus.IEvent;
import com.bon.eventbus.RxBus;
import com.bon.logger.Logger;
import com.bon.permission.PermissionUtils;
import com.bon.sharepreferences.AppPreferences;
import com.crashlytics.android.Crashlytics;
import com.mc.books.BuildConfig;
import com.mc.books.R;
import com.mc.books.fragments.notification.CustomNotificationOpenedHandle;
import com.mc.books.fragments.notification.CustomNotificationReceiverHandle;
import com.mc.di.AppComponent;
import com.mc.di.AppModule;
import com.mc.di.DaggerAppComponent;
import com.mc.events.BackgroundEvent;
import com.mc.interactors.service.IApiService;
import com.mc.models.User;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;
import com.onesignal.OneSignal;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import java8.util.function.Consumer;

import static com.mc.utilities.Constant.KEY_USER_INFO;


public class AppContext extends ExtApplication {
    private static final String TAG = AppContext.class.getSimpleName();

    private AppComponent component;
    private static Realm realm;
    private static ThinDownloadManager downloadManager;
    private String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    static Map<String, String> headers = new HashMap<>();

    @Inject
    IApiService apiService;

    @Inject
    RxBus<IEvent> rxBus;

    @Override
    public void onCreate() {
        super.onCreate();
        // dagger
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        component.inject(this);

        //onesignal
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new CustomNotificationOpenedHandle(getApplicationContext()))
                .setNotificationReceivedHandler(new CustomNotificationReceiverHandle(apiService, rxBus))
                .autoPromptLocation(true)
                .init();


        //fabric
        Fabric.with(this, new Crashlytics());

        // logger
        Logger.setEnableLog(BuildConfig.DEBUG);

        // init fragment
        FragmentUtils.setContainerViewId(R.id.fl_content);

        // catch all exception
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread thread, Throwable e) {
//                Log.e("AppContext::" + thread.getClass().getName(),
//                        e.getMessage() + "\n" +
//                                "Error in " + Arrays.toString(e.getCause().getStackTrace()));
//            }
//        });

        Realm.init(this);
        RealmConfiguration config;

        // extenal storage
        if (PermissionUtils.hasPermissions(this, perms)) {
            config = new RealmConfiguration.Builder()
                    .directory(new File(AppUtils.getRealmPath(this)))
                    .name("mcb.realm")
                    .deleteRealmIfMigrationNeeded()
                    .build();
        } else {
            //internal storage
            config = new RealmConfiguration.Builder()
                    .name("mcb.realm")
                    .deleteRealmIfMigrationNeeded()
                    .build();
        }

        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        downloadManager = new ThinDownloadManager(1);

        headers.put(Constant.DEVICE, Constant.ANDROID);
    }

    //setup for first time install app and request permission
    public static void setConfigRealm(Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .directory(new File(AppUtils.getRealmPath(context)))
                .name("mcb.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
    }

    public static Realm getRealm() {
        return realm;
    }

    public static void setAppLycicleTraining(RxBus rxBus) {
        ExtApplication.getInstance().setLicycleApp(s -> {
            if (rxBus != null)
                rxBus.send(new BackgroundEvent(Constant.KEY_BACKGROUND_TRAINING));
        });
    }

    public static ThinDownloadManager getDownloadManager() {
        return downloadManager;
    }

    @Nullable
    public static AppComponent getComponentFromContext(@Nullable Context context) {
        if (context == null) return null;

        AppComponent component;
        if (context instanceof AppContext) {
            component = ((AppContext) context).getComponent();
        } else {
            component = ((AppContext) context.getApplicationContext()).getComponent();
        }

        return component;
    }

    @Nullable
    public static AppContext from(@Nullable Context context) {
        if (context == null) return null;

        if (context instanceof AppContext) {
            return (AppContext) context;
        }

        return (AppContext) context.getApplicationContext();
    }

    public static void ifPresent(@Nullable Context context, Consumer<AppContext> contextConsumer) {
        if (context == null || contextConsumer == null) return;

        if (context instanceof AppContext) {
            contextConsumer.accept((AppContext) context);
            return;
        }

        contextConsumer.accept((AppContext) context.getApplicationContext());
    }

    public AppComponent getComponent() {
        return component;
    }

    public static String getTokenApp() {
        String KEY_HEADER_AUTH = Constant.BEARER;
        Log.e("TOKEN: ", KEY_HEADER_AUTH + AppPreferences.getInstance(getInstance()).getString(Constant.ACCESS_TOKEN));
        return KEY_HEADER_AUTH + AppPreferences.getInstance(getInstance()).getString(Constant.ACCESS_TOKEN);
    }

    public static String getUserId() {
        User user = AppPreferences.getInstance(getInstance()).getJson(KEY_USER_INFO, User.class);
        if (user != null)
            return user.getId();
        else return "unknown";
    }

    public static Map<String, String> getHeader() {
        return headers;
    }
}
