package com.mc.books.fragments.more.tabmore;

import android.content.Context;

import com.bon.sharepreferences.AppPreferences;
import com.mc.books.BuildConfig;
import com.mc.common.presenters.BaseDataPresenter;
import com.mc.di.AppComponent;
import com.mc.utilities.Constant;

import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.mc.utilities.Constant.KEY_REFRESH_TOKEN;


public class MorePresenter<V extends IMoreView> extends BaseDataPresenter<V> implements IMorePresenter<V> {

    //    private final String URL_LOGOUT = "http://103.101.160.49:8080/auth/realms/MCB686/protocol/openid-connect/logout";
    private final String URL_LOGOUT = "https://sso.mentor.vn/auth/realms/" + BuildConfig.SSO + "/protocol/openid-connect/logout";
    private final String CLIENT_ID = "mobile";

    protected MorePresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void logout(Context context) {
        getOptView().doIfPresent(v -> {
            if (!v.isValidNetwork()) return;

            v.onShowLoading(true);
            apiService.logout(URL_LOGOUT, AppPreferences.getInstance(context).getString(KEY_REFRESH_TOKEN), CLIENT_ID).subscribeOn(Schedulers.io()).subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {
                    v.onShowLoading(false);
                    v.logoutSuccess();
                }

                @Override
                public void onError(Throwable e) {
                    v.onShowLoading(false);
                }

                @Override
                public void onNext(String s) {

                }
            });
        });

    }
}
