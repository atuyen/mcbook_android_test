package com.mc.books.fragments.more.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.models.User;

public interface IUserProfilePresenter<V extends MvpView> extends MvpPresenter<V> {
    void getListCity();

    void updateUser(User user);

    void uploadAvatar(String uri, Context context);

}
