package com.mc.books.fragments.home.training;

import com.mc.common.views.IBaseView;
import com.mc.models.home.ItemTrainingResponse;
import com.mc.models.home.TrainingResponse;

import java.util.List;

public interface ITrainingView extends IBaseView {
    void onShowLoading(boolean loading);

    void getListTrainingSuccess(List<TrainingResponse> trainingResponses);

    void getLogTrainingSuccess(ItemTrainingResponse trainingResponse);
}
