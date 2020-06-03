package com.bon.customview.customtabmore;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.StringUtils;
import com.bon.util.TypefacesUtils;

public class CustomTabMore extends LinearLayout {
    Drawable imageicon;
    String txttabmore;
    TextView mTxt;
    ImageView imageView;

    public CustomTabMore(Context context) {
        super(context);
    }

    public CustomTabMore(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyAttributes(context, attrs);
    }

    public CustomTabMore(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyAttributes(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomTabMore(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyAttributes(context, attrs);
    }

    private void applyAttributes(Context context, AttributeSet attrs) {
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTabmoreAttrs);
            imageicon = typedArray.getDrawable(R.styleable.CustomTabmoreAttrs_imageIcon);
            txttabmore = typedArray.getString(R.styleable.CustomTabmoreAttrs_txtTextTabMore);
            typedArray.recycle();
        } catch (Exception e) {
        }

        if (!TextUtils.isEmpty(txttabmore)) {
            mTxt.setText(txttabmore);
        }

        if (null != imageicon) {
            imageView.setImageDrawable(imageicon);
        }
    }
}
