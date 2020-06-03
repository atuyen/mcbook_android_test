package com.mc.common.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.di.AppComponent;
import com.mc.interactors.IDataModule;
import com.mc.interactors.database.IDbModule;
import com.bon.eventbus.IEvent;
import com.bon.eventbus.RxBus;
import com.bon.interfaces.Optional;
import com.mc.interactors.service.IApiService;

import javax.inject.Inject;

/**
 * Created by dangpp on 2/21/2018.
 */

public abstract class BaseDataPresenter<V extends MvpView> extends MvpBasePresenter<V>
        implements IBasePresenter {

    @Inject
    public IDataModule dataModule;
    @Inject
    public IDbModule dbModule;
    @Inject
    public RxBus<IEvent> bus;

    @Inject
    protected IApiService apiService;

    /**
     * @param appComponent
     */
    protected BaseDataPresenter(AppComponent appComponent) {
        // leave casting to match generic type for Dagger2
        appComponent.inject((BaseDataPresenter<MvpView>) this);
    }

    /**
     * Wrap view getter to optional value.
     *
     * @return {@link Optional} of view.
     */
    @NonNull
    protected Optional<V> getOptView() {
        return new Optional<>(getView());
    }

    @Override
    public void processArguments(Bundle arguments) {

    }

    @Override
    public void saveInstanceState(Bundle bundle) {

    }

    @Override
    public void restoreInstanceState(Bundle bundle) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        bus.unSubscribe(this);
    }
}
