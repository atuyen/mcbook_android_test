package com.bon.customview.scrollview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by dangpp on 10/13/2016.
 */

public class ExtScrollView extends ScrollView {
    private ExtScrollViewListener extScrollViewListener;

    public ExtScrollView(Context context) {
        super(context);
    }

    public ExtScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ExtScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.extScrollViewListener != null) {
            this.extScrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public void setExtScrollViewListener(ExtScrollViewListener extScrollViewListener) {
        this.extScrollViewListener = extScrollViewListener;
    }

    public interface ExtScrollViewListener {
        void onScrollChanged(ExtScrollView scrollView, int x, int y, int oldX, int oldY);
    }
}
