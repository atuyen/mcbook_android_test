package com.mc.books.fragments.gift.resultExam;

import com.mc.common.views.IBaseView;
import com.mc.models.home.DetailQuestion;

import java.util.List;

public interface IResultExamView extends IBaseView {

    void onShowLoading(boolean loading);

    void sendLogExamSuccess(float point);

    void sendLogExamError();
}
