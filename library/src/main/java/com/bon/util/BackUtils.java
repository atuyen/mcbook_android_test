package com.bon.util;

import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;

import com.bon.logger.Logger;

/**
 * Created by dangpp on 10/27/2016.
 */

public class BackUtils {
    private static final String TAG = BackUtils.class.getSimpleName();

    // check status
    private static boolean isDoubleClick = false;

    /**
     * @param activity
     * @param messageConfirm
     */
    public static void onClickExit(Activity activity, String messageConfirm) {
        try {
            // double tap to exit app
            if (BackUtils.isDoubleClick) {
                System.exit(0);
                return;
            }

            BackUtils.isDoubleClick = true;
            ToastUtils.showToast(activity, messageConfirm, Gravity.BOTTOM);
            new Handler().postDelayed(() -> BackUtils.isDoubleClick = false, 2000);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
