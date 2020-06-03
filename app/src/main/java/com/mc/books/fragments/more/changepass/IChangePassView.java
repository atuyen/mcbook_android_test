package com.mc.books.fragments.more.changepass;

import com.mc.common.views.IBaseView;

public interface IChangePassView extends IBaseView {
    void onShowLoading(boolean isShow);

    void onChangePassSuccess();

    void onError(int code);
}
