package com.bon.util;

import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import com.bon.logger.Logger;

/**
 * Created by Dang Pham Phu on 2/2/2017.
 */

public class TextUtils {
    private static final String TAG = TextUtils.class.getSimpleName();

    /**
     * set text appearance for view
     *
     * @param context
     * @param tvTextView
     * @param resourceId (StyleRes)
     */
    public static void setTextAppearance(Context context, TextView tvTextView, int resourceId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvTextView.setTextAppearance(resourceId);
            } else {
                tvTextView.setTextAppearance(context, resourceId);
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }
}
