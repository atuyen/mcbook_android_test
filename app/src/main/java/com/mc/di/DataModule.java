package com.mc.di;

import android.os.Handler;

import com.mc.events.SignInEvent;
import com.mc.interactors.IDataModule;
import com.bon.eventbus.IEvent;
import com.bon.eventbus.RxBus;
import com.mc.interactors.database.IDbModule;
import com.mc.interactors.service.IApiService;

import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import java8.util.function.Consumer;
import rx.functions.Action0;

/**
 * Created by dangpp on 2/9/2018.
 */
@Module
public class DataModule implements IDataModule {
    private static final String TAG = DataModule.class.getSimpleName();

    private final AppComponent component;
    private final Handler handler = new Handler();

    @Inject
    RxBus<IEvent> bus;

    @Inject
    IApiService apiService;

    @Inject
    IDbModule dbModule;

    public DataModule(AppComponent component) {
        this.component = component;
        component.inject(this);
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public void getUsers(Consumer<List<String>> consumer) {
        Action0 f;
    }

    public void sentMessage(){
        bus.send(new SignInEvent("fdfdfdf"));

    }
}
