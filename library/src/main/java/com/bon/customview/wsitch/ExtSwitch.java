package com.bon.customview.wsitch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Switch;

import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.StringUtils;
import com.bon.util.TypefacesUtils;

/**
 * Created by Dang on 7/18/2016.
 */
public class ExtSwitch extends Switch {
    private static final String TAG = ExtSwitch.class.getSimpleName();

    public ExtSwitch(Context context) {
        super(context);
    }

    public ExtSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttributes(context, attrs);
    }

    public ExtSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyAttributes(context, attrs);
    }

    @SuppressLint("NewApi")
    public ExtSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyAttributes(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     */
    private void applyAttributes(Context context, AttributeSet attrs) {
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtSwitchView);
            String fontPath = typedArray.getString(R.styleable.ExtSwitchView_switchFontAssetName);
            if (StringUtils.isEmpty(fontPath)) fontPath = TypefacesUtils.FONT_DEFAULT;
            this.setTypeface(TypefacesUtils.get(getContext(), fontPath));
            this.setPaintFlags(this.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            typedArray.recycle();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
