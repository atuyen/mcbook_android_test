package com.mc.books.fragments.home.statistics;

import com.mc.common.views.IBaseView;
import com.mc.models.home.BookProcess;
import com.mc.models.home.BookStatistic;

import java.util.List;

public interface IStatisticsView extends IBaseView {
    void onShowLoading(boolean isShow);

    void getListBookStatistic(List<BookStatistic> bookStatistics);

    void getListBookProcess(List<BookProcess> bookProcesses);
}
