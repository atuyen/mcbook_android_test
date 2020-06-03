package com.mc.books.fragments.gift.myGift;

import android.view.View;

import com.mc.common.views.IBaseView;
import com.mc.models.gift.AddGift;
import com.mc.models.gift.CategoryGift;

import java.util.List;

public interface IGiftView extends IBaseView {
    void onShowLoading(boolean isShow);

    void onShowDialogDelete(boolean isShow, View view, int position);

    void onGetCategoryGiftSuccess(List<CategoryGift> categoryGift);

    void onCreateGiftFail(int stringError);

    void onCreateGiftSuccess(AddGift addGift);

    void onDeleteGiftSuccess();
}
