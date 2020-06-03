package com.bon.customview.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import com.bon.logger.Logger;

/**
 * Created by Dang on 9/18/2015.
 */
public class ExtExpListView extends ExpandableListView {
    private static final String TAG = ExtExpListView.class.getSimpleName();

    /**
     * @param context
     */
    public ExtExpListView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public ExtExpListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ExtExpListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int heightSpec;
            if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
                heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 1, MeasureSpec.AT_MOST);
            } else {
                // Any other height should be respected as is.
                heightSpec = heightMeasureSpec;
            }

            refreshDrawableState();
            super.onMeasure(widthMeasureSpec, heightSpec);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }
}
