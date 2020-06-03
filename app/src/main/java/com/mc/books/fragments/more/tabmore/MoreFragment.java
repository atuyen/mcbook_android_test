package com.mc.books.fragments.more.tabmore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.bon.sharepreferences.AppPreferences;
import com.bon.util.ActivityUtils;
import com.mc.books.R;
import com.mc.books.activity.InStructionActivity;
import com.mc.books.activity.SignInActivity;
import com.mc.books.dialog.ErrorConfirmDialog;
import com.mc.books.fragments.more.changepass.ChangePassFragment;
import com.mc.books.fragments.more.information.InformationFragment;
import com.mc.books.fragments.more.leadboard.LeadBroadFragment;
import com.mc.books.fragments.more.policy.PolicyFragment;
import com.mc.books.fragments.more.profile.UserProfileFragment;
import com.mc.books.fragments.more.setting.SettingFragment;
import com.mc.books.fragments.more.support.SupportFragment;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.customizes.menus.ItemMenu;
import com.mc.models.User;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;
import com.mc.utilities.FragmentUtils;
import com.onesignal.OneSignal;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.mc.utilities.Constant.KEY_USER_INFO;

public class MoreFragment extends BaseMvpFragment<IMoreView, IMorePresenter<IMoreView>> implements IMoreView {
    @BindView(R.id.imgAvartar)
    CircleImageView imgAvartar;
    @BindView(R.id.txtName)
    ExtTextView txtName;
    @BindView(R.id.llCharts)
    ItemMenu llCharts;
    @BindView(R.id.llSetting)
    ItemMenu llSetting;
    @BindView(R.id.llChangePassword)
    ItemMenu llChangePassword;
    @BindView(R.id.llUsermanual)
    ItemMenu llUsermanual;
    @BindView(R.id.llInformation)
    ItemMenu llInformation;
    @BindView(R.id.llLegal)
    ItemMenu llLegal;
    @BindView(R.id.llSupport)
    ItemMenu llSupport;
    @BindView(R.id.llSignout)
    ItemMenu llSignout;
    @BindView(R.id.ivEdit)
    ImageView ivEdit;
    private User user;

    public static MoreFragment newInstance() {
        Bundle args = new Bundle();
        MoreFragment fragment = new MoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public IMorePresenter<IMoreView> createPresenter() {
        return new MorePresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        user = AppPreferences.getInstance(getAppContext()).getJson(KEY_USER_INFO, User.class);
        AppPreferences.getInstance(getAppContext()).putString(Constant.KEY_MORE, Constant.KEY_MORE);
        if (user != null) {
            txtName.setText(user.getFullname().isEmpty() ? user.getFirstName() : user.getFullname());
            AppUtils.setImageGlide(getAppContext(), user.getAvatar(), R.drawable.ic_default_avatar, imgAvartar);
        }
    }

    @Override
    public int getResourceId() {
        return R.layout.more_fragment;
    }

    @Override
    public int getTitleId() {
        return R.string.app_name;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.hide();
    }

    @OnClick({R.id.ivEdit, R.id.llCharts, R.id.llSetting, R.id.llChangePassword, R.id.llUsermanual, R.id.llInformation, R.id.llLegal, R.id.llSupport, R.id.llSignout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivEdit:
                FragmentUtils.replaceFragment(mActivity, UserProfileFragment.newInstance(user), fragment -> mMainActivity.fragments.add(fragment));
                break;
            case R.id.llCharts:
                FragmentUtils.replaceFragment(mActivity, LeadBroadFragment.newInstance(), fragment -> mMainActivity.fragments.add(fragment));
                break;
            case R.id.llSetting:
                FragmentUtils.replaceFragment(mActivity, SettingFragment.newInstance(), fragment -> mMainActivity.fragments.add(fragment));
                break;
            case R.id.llChangePassword:
                FragmentUtils.replaceFragment(mActivity, ChangePassFragment.newInstance(), fragment -> mMainActivity.fragments.add(fragment));
                break;
            case R.id.llUsermanual:
                ActivityUtils.startActivity(InStructionActivity.class);
                break;
            case R.id.llInformation:
                FragmentUtils.replaceFragment(getActivity(), InformationFragment.newInstance(), fragment -> mMainActivity.fragments.add(fragment));
                break;
            case R.id.llLegal:
                FragmentUtils.replaceFragment(getActivity(), PolicyFragment.newInstance(), fragment -> mMainActivity.fragments.add(fragment));
                break;
            case R.id.llSupport:
                FragmentUtils.replaceFragment(getActivity(), SupportFragment.newInstance(), fragment -> mMainActivity.fragments.add(fragment));
                break;
            case R.id.llSignout:
                showDialogLogout();
                break;
        }
    }

    private void clearDeviceId() {
        OneSignal.idsAvailable((userId, registrationId) -> apiService.clearDeviceId(userId).observeOn(AndroidSchedulers.mainThread())
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
                }));
    }

    private void showDialogLogout() {
        clearDeviceId();
        ErrorConfirmDialog errorConfirmDialog = new ErrorConfirmDialog(getContext(), getContext().getString(R.string.yes), getContext().getString(R.string.logout_confirm), v -> presenter.logout(getContext()));
        errorConfirmDialog.show();
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void logoutSuccess() {
        try {
            clearPreference();
            OneSignal.setSubscription(false);
            startActivity(new Intent(getAppContext(), SignInActivity.class));
            getActivity().finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearPreference() {
        try {
            AppPreferences.getInstance(getContext()).putJson(KEY_USER_INFO, null);
            AppPreferences.getInstance(getAppContext()).putString(Constant.KEY_TOKEN, "");
            AppPreferences.getInstance(getAppContext()).putString(Constant.KEY_ANDROID_SHARE, "");
            AppPreferences.getInstance(getAppContext()).putString(Constant.KEY_REDIRECT_URL, "");
            AppPreferences.getInstance(getAppContext()).putString(Constant.KEY_REFRESH_TOKEN, "");
            AppPreferences.getInstance(getAppContext()).putString(Constant.KEY_ACCESS_TOKEN, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
