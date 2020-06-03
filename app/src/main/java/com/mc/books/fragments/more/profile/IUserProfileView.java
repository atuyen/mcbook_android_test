package com.mc.books.fragments.more.profile;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.common.views.IBaseView;
import com.mc.models.User;
import com.mc.models.more.City;

import java.util.List;

public interface IUserProfileView extends IBaseView {
    void onShowLoading(boolean isShow);

    void getListCitySuccess(List<City> cityList);

    void onPhoneError();

    void onNameEmpty();

    void onPhoneEmpty();

    void upLoadSuccess(String uri);

    void updateUserSuccess(User user);

}
