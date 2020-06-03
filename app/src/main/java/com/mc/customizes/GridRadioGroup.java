package com.mc.customizes;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.mc.adapter.GridRadioGroupAdapter;
import com.mc.books.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GridRadioGroup extends LinearLayout {

    Unbinder unbinder;
    @BindView(R.id.txtTitle)
    ExtTextView txtTitle;
    @BindView(R.id.rvRadio)
    RecyclerView rvRadio;

    private GridRadioGroupAdapter gridRadioGroupAdapter;

    public GridRadioGroup(Context context) {
        super(context);
        initView(context, null);
    }

    public GridRadioGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public GridRadioGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, null);
    }

    public GridRadioGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, null);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_radio_group, this);
        unbinder = ButterKnife.bind(this, view);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GridRadioGroup);
        txtTitle.setText(typedArray.getString(R.styleable.GridRadioGroup_gridTitle));

        gridRadioGroupAdapter = new GridRadioGroupAdapter(context, typedArray.getInt(R.styleable.GridRadioGroup_gridDefaultChecked, 0));
        rvRadio.setLayoutManager(new GridLayoutManager(context, typedArray.getInt(R.styleable.GridRadioGroup_gridNumber, 2)));
        rvRadio.setAdapter(gridRadioGroupAdapter);
        rvRadio.setNestedScrollingEnabled(false);
    }

    public void setDataRadio(List<String> stringList) {
        gridRadioGroupAdapter.setDataList(stringList);
    }

    public int getCheckedItem() {
        return gridRadioGroupAdapter.getCheckedItem();
    }

    public String getValueCheckedItem() {
        return gridRadioGroupAdapter.getValueCheckedItem();
    }
}
