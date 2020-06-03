package com.bon.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;

import com.bon.logger.Logger;

/**
 * Created by Dang on 6/22/2016.
 */
public class FontUtils {
    private static final String TAG = FontUtils.class.getSimpleName();

    /**
     * set custom font for view
     *
     * @param fontPath from asset, example: fonts/abc.ttf
     */
    public static void setCustomTypeface(Context context, TextView view, String fontPath) {
        try {
            Typeface font = TypefacesUtils.get(context, fontPath);
            if (font != null) {
                view.setTypeface(font);
                view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
