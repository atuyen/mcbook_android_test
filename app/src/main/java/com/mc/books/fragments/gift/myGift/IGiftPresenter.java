package com.mc.books.fragments.gift.myGift;

import android.view.View;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.mc.models.gift.CategoryGift;

import java.util.List;

public interface IGiftPresenter<V extends MvpView> extends MvpPresenter<V> {
    void onSearchGift(String keyword, List<CategoryGift> categoryGifts);

    void onShowDialogDelete(boolean isShow, View view, int position);

    void onGetCategoryGift();

    void onCreateGiftCode(String code);

    void deleteGift(int id);
}
