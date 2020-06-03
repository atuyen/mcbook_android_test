package com.mc.di;

import com.mc.interactors.database.IDbModule;
import com.bon.eventbus.IEvent;
import com.bon.eventbus.RxBus;

import java.util.List;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by dangpp on 2/9/2018.
 */

@Module
public class DbModule implements IDbModule {
    private static final String TAG = DbModule.class.getSimpleName();

    @Inject
    RxBus<IEvent> bus;

    public DbModule(AppComponent component) {
        component.inject(this);
    }

    @Override
    public List<String> getUsers() {
        return null;
    }

    @Override
    public void insertUsers(List<String> users) {

    }
}
