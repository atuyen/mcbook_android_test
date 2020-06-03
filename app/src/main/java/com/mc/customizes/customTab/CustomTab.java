package com.mc.customizes.customTab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.mc.books.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CustomTab extends LinearLayout {
    private static final String TAG = CustomTab.class.getSimpleName();

    @BindView(R.id.tvTab)
    ExtTextView tvTab;

    Unbinder unbinder;

    public CustomTab(Context context) {
        super(context);
        initView(context, null);
    }

    public CustomTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CustomTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    int textColorActive;
    int textColorInActive;
    int colorActive;
    int colorInActive;

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabs, this);
        unbinder = ButterKnife.bind(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTab);

        // active
        boolean isActive = typedArray.getBoolean(R.styleable.CustomTab_tabActive, false);
        textColorActive = typedArray.getInteger(R.styleable.CustomTab_tabTextActiveColor, R.color.color_white);
        textColorInActive = typedArray.getInteger(R.styleable.CustomTab_tabTextInactiveColor, R.color.colorTextGray);
        colorActive = typedArray.getInteger(R.styleable.CustomTab_tabActiveColor, R.drawable.bg_btn_orange);
        colorInActive = typedArray.getInteger(R.styleable.CustomTab_tabInactiveColor, R.drawable.bg_btn_white);
        String text = typedArray.getString(R.styleable.CustomTab_tabText);

        tvTab.setText(text);
        setActiveMode(isActive);
        typedArray.recycle();
    }

    public void setActiveMode(boolean isActive) {
        if (isActive) {
            tvTab.setTextColor(getResources().getColor(textColorActive));
            tvTab.setBackground(getResources().getDrawable(colorActive));
        } else {
            tvTab.setTextColor(getResources().getColor(textColorInActive));
            tvTab.setBackground(getResources().getDrawable(colorInActive));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Optional.from(unbinder).doIfPresent(Unbinder::unbind);
    }

}
