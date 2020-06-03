package com.mc.books.fragments.home.doTraining;

import com.mc.common.views.IBaseView;
import com.mc.models.home.DetailTraining;
import com.mc.models.home.RecordItem;

import java.util.List;

public interface IDoTrainingView extends IBaseView {
    void onShowLoading(boolean loading);

    void getDetailTrainingSuccess(DetailTraining detailTraining);

    void getDetailTrainingError(String message);
}
