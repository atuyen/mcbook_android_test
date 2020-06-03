package com.mc.common.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.eventbus.IEvent;
import com.bon.eventbus.RxBus;
import com.bon.interfaces.Optional;
import com.bon.network.NetworkUtils;
import com.bon.util.KeyboardUtils;
import com.bon.util.StringUtils;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.application.AppContext;
import com.mc.books.R;
import com.mc.books.activity.MainActivity;
import com.mc.books.dialog.ErrorBoxDialog;
import com.mc.common.activities.BaseAppCompatActivity;
import com.mc.common.views.IBaseView;
import com.mc.di.AppComponent;
import com.mc.events.NetworkEvent;
import com.mc.interactors.IDataModule;
import com.mc.interactors.database.IDbModule;
import com.mc.interactors.service.IApiService;


import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dangpp on 2/21/2018.
 */

public abstract class BaseMvpFragment<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpFragment<V, P> implements IBaseFragment, IResourceFragment, IBaseView {
    private static final String TAG = BaseMvpFragment.class.getSimpleName();

    protected BaseAppCompatActivity mActivity;
    protected MainActivity mMainActivity;

    @Inject
    protected RxBus<IEvent> bus;

    @Inject
    protected IDataModule dataModule;

    @Inject
    protected IDbModule dbModule;

    @Inject
    protected IApiService apiService;

    // unbind butter knife
    private Unbinder unbinder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseAppCompatActivity) {
            this.mActivity = (BaseAppCompatActivity) activity;
        }
        if (activity instanceof MainActivity) {
            this.mMainActivity = (MainActivity) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inject component
        getAppContext().getComponent().inject((BaseMvpFragment<MvpView, MvpPresenter<MvpView>>) this);

        // retain this fragment when activity is re-initialized
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getResourceId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // update toolbar
        Optional.from(mActivity.getAppSupportActionBar())
                .doIfPresent(actionBar -> initToolbar(actionBar));
    }

    @Override
    public void onResume() {
        super.onResume();
        // update title
        if (StringUtils.isEmpty(getTitleString())) {
            mActivity.setToolbarTitle(getTitleId());
        } else {
            mActivity.setToolbarTitle(getTitleString());
        }
    }

    @Override
    public void bindButterKnife(View view) {
        Optional.from(view).doIfPresent(v -> unbinder = ButterKnife.bind(this, v));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // hide loading, keyboard
        showProgress(false);
        KeyboardUtils.hideSoftKeyboard(mActivity);

        // unbind butter knife
        Optional.from(unbinder).doIfPresent(u -> u.unbind());
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            mActivity.showProgressDialog();
        } else {
            mActivity.hideProgressDialog();
        }
    }

    @Override
    public int getTitleId() {
        return 0;
    }

    @Override
    public String getTitleString() {
        return "";
    }

    @Override
    public AppContext getAppContext() {
        return mActivity.getAppContext();
    }

    @Override
    public AppComponent getAppComponent() {
        return mActivity.getAppContext().getComponent();
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(false);
        supportActionBar.setHomeAsUpIndicator(0);
        supportActionBar.setIcon(0);
        supportActionBar.show();
    }

    @Override
    public boolean isValidNetwork() {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            EventBus.getDefault().post(new NetworkEvent(false));
            return false;
        }
        return true;
    }

    @Override
    public void showNetworkRequire() {
        ErrorBoxDialog errorBoxDialog = new ErrorBoxDialog(getContext(), getString(R.string.connect_internet_touse));
        errorBoxDialog.show();
    }
}
