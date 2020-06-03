package com.mc.di;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.application.AppContext;
import com.mc.books.fragments.notification.CustomNotificationReceiverHandle;
import com.mc.common.activities.BaseAppCompatActivity;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.common.presenters.BaseDataPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by dangpp on 2/9/2018.
 */

@Singleton
@Component(modules = {
        ApiModule.class,
        DbModule.class,
        DataModule.class,
        AppModule.class
})
public interface AppComponent {
    void inject(DataModule dataModule);

    void inject(DbModule dbModule);

    void inject(AppContext appContext);

    void inject(BaseAppCompatActivity activity);

    void inject(BaseDataPresenter<MvpView> presenter);

    void inject(BaseMvpFragment<MvpView, MvpPresenter<MvpView>> fragment);

    void inject(CustomNotificationReceiverHandle notificationReceiverHandle);
}
