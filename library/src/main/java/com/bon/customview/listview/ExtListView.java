package com.bon.customview.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.bon.logger.Logger;

/**
 * Created by Dang on 9/16/2015.
 */
public class ExtListView extends ListView {
    private static final String TAG = ExtListView.class.getSimpleName();

    /**
     * @param context
     */
    public ExtListView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public ExtListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ExtListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int heightSpec;
            if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
                heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 1,
                        MeasureSpec.AT_MOST);
            } else {
                // Any other height should be respected as is.
                heightSpec = heightMeasureSpec;
            }

            super.onMeasure(widthMeasureSpec, heightSpec);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }
}