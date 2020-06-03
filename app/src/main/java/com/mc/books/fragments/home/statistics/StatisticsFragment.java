package com.mc.books.fragments.home.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bon.util.TypefacesUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.CustomDecimalFormatter;
import com.mc.adapter.BookStatisticAdapter;
import com.mc.books.R;
import com.mc.common.fragments.BaseMvpFragment;
import com.mc.customizes.chart.CustomDate;
import com.mc.customizes.chart.EmptyData;
import com.mc.models.home.BookProcess;
import com.mc.models.home.BookStatistic;
import com.mc.utilities.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StatisticsFragment extends BaseMvpFragment<IStatisticsView, IStatisticsPresenter<IStatisticsView>> implements IStatisticsView {
    public static StatisticsFragment newInstance() {
        Bundle args = new Bundle();
        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<BookProcess> bookProcessList = new ArrayList<>();
    @BindView(R.id.chart)
    LineChart lineChart;
    @BindView(R.id.rvBookStatistic)
    RecyclerView rvBookStatistic;
    private BookStatisticAdapter bookStatisticAdapter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initData();
        presenter.onLoadBookStatistic();
    }

    private void initData() {
        bookStatisticAdapter = new BookStatisticAdapter(getAppContext());
        rvBookStatistic.setLayoutManager(new LinearLayoutManager(getAppContext()));
        rvBookStatistic.setAdapter(bookStatisticAdapter);
        rvBookStatistic.setNestedScrollingEnabled(false);
    }

    private void initChart() {
        lineChart.setVisibility(View.VISIBLE);
        List<String> listDate = new ArrayList<>();
        List<Integer> listPoint = new ArrayList<>();
        for (int i = 0; i < bookProcessList.size(); i++) {
            listDate.add(bookProcessList.get(i).getDate());
            listPoint.add(bookProcessList.get(i).getPoint());
        }
        List<Entry> entries = new ArrayList<>();
        for (int j = 0; j < listDate.size(); j++) {
            float val = listPoint.get(j);
            entries.add(new Entry(j, val));
        }
        String fontPath = "fonts/SF-Pro-Display-Regular.otf";

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setValueFormatter(new CustomDecimalFormatter());
        dataSet.setColor(getContext().getResources().getColor(R.color.colorStatictisLine));
        dataSet.setValueTextColor(getContext().getResources().getColor(R.color.colorStatictisText));
        dataSet.setValueTypeface(TypefacesUtils.get(getContext(), fontPath));
        dataSet.setValueTextSize(AppUtils.isTablet(getContext()) ? 17 : 14);
        dataSet.setCircleColor(getContext().getResources().getColor(R.color.colorStatictisCircle));
        dataSet.setCircleHoleColor(getContext().getResources().getColor(R.color.colorStatictisCircle));
        dataSet.setCircleHoleRadius(4);
        dataSet.setCircleRadius(4);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setCubicIntensity(0.2f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setAxisLineColor(Color.GRAY);
        lineChart.getXAxis().setGridColor(Color.GRAY);
        lineChart.getLegend().setEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setExtraBottomOffset(50);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new CustomDate(listDate));
        xAxis.setTypeface(TypefacesUtils.get(getContext(), fontPath));
        xAxis.setTextSize(AppUtils.isTablet(getContext()) ? 13 : 11);
        xAxis.setTextColor(Color.BLACK);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setValueFormatter(new EmptyData());
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setValueFormatter(new EmptyData());

        lineChart.invalidate();
    }

    @NonNull
    @Override
    public IStatisticsPresenter<IStatisticsView> createPresenter() {
        return new StatisticsPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.statistic_fragment;
    }

    @Override
    public int getTitleId() {
        return R.string.statistic;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public void showProgress(boolean show) {
        if (show) mActivity.showProgressDialog();
        else mActivity.hideProgressDialog();
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void getListBookStatistic(List<BookStatistic> bookStatistics) {
        bookStatisticAdapter.setDataList(bookStatistics);
    }

    @Override
    public void getListBookProcess(List<BookProcess> bookProcesses) {
        bookProcessList.addAll(bookProcesses);
        initChart();
    }

}
