package com.bon.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.bon.logger.Logger;

public class ToastUtils {
    private static final String TAG = ToastUtils.class.getSimpleName();

    /**
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        try {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public static void showToast(Context context, String message, int gravity, int duration) {
        try {
            Toast toast = Toast.makeText(context, message, duration);
            toast.setGravity(gravity, 0, 0);
            toast.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param context
     * @param message
     * @param gravity
     */
    public static void showToast(Context context, String message, int gravity) {
        try {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(gravity, 0, 0);
            toast.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public static void showToast(Context context, String message, int gravity,
                                 int xOffset, int yOffset) {
        try {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(gravity, xOffset, yOffset);
            toast.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param context
     * @param view
     */
    public static void showToast(Context context, View view) {
        try {
            Toast toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(view);
            toast.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param context
     * @param view
     * @param gravity
     */
    public static void showToast(Context context, View view, int gravity) {
        try {
            Toast toast = new Toast(context);
            toast.setGravity(gravity, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(view);
            toast.setMargin(0, -GeneralUtils.getStatusBarHeight(context));
            toast.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
