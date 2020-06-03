package com.mc.books.fragments.gift.examinfo;

import com.mc.common.views.IBaseView;
import com.mc.models.home.DetailQuestion;
import java.util.List;
import com.mc.models.gift.InfomationExam;


public interface IExamInfomationView extends IBaseView {

    void onShowLoading(boolean loading);

    void getListQuestionExamSuccess(List<DetailQuestion> detailQuestionList);

    void getListQuestionExamError();

    void getInfomationExam(InfomationExam infomationExam);

    void getInfomationExamError();

}
