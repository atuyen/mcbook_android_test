package com.bon.customview.numberpicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.FontUtils;
import com.bon.util.TextUtils;

/**
 * Created by dangpp on 8/14/2017.
 */

public class ExtNumberPicker extends NumberPicker {
    private static final String TAG = ExtNumberPicker.class.getSimpleName();

    public ExtNumberPicker(Context context) {
        super(context);
    }

    public ExtNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExtNumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    private void updateView(View view) {
        try {
            if (view instanceof TextView) {
                TextView tvTextView = (TextView) view;
                FontUtils.setCustomTypeface(getContext(), tvTextView, getResources().getString(R.string.font_regular));
                TextUtils.setTextAppearance(getContext(), tvTextView, R.style.StyleContent);
            }
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }
    }
}
