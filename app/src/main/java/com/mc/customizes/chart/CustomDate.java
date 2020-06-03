package com.mc.customizes.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

public class CustomDate implements IAxisValueFormatter {
    private List<String> mListDate;

    public CustomDate(List<String> listDate) {
        this.mListDate = listDate;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mListDate.get((int) value % mListDate.size());
    }
}
