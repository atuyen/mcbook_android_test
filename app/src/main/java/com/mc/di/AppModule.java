package com.mc.di;

import android.content.Context;

import com.bon.eventbus.IEvent;
import com.bon.eventbus.RxBus;
import com.mc.application.AppContext;
import com.mc.interactors.IDataModule;
import com.mc.interactors.database.IDbModule;
import com.mc.interactors.service.IApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dangpp on 2/9/2018.
 */

@Module
public class AppModule {
    private final AppContext appContext;

    public AppModule(AppContext appContext) {
        this.appContext = appContext;
    }

    @Singleton
    @Provides
    public RxBus<IEvent> provideEvenBus() {
        return new RxBus<>();
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return appContext;
    }

    @Singleton
    @Provides
    public AppContext provideAppContext() {
        return appContext;
    }

    @Singleton
    @Provides
    public IDataModule provideDataModule() {
        return new DataModule(appContext.getComponent());
    }

    @Singleton
    @Provides
    public IDbModule provideDbModule() {
        return new DbModule(appContext.getComponent());
    }

    @Singleton
    @Provides
    public ApiModule provideApiModule() {
        return new ApiModule();
    }
}
