package com.bon.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.bon.library.R;
import com.bon.logger.Logger;

@SuppressWarnings("ALL")
public class DialogUtils {
    private static final String TAG = DialogUtils.class.getSimpleName();

    /**
     * @param context
     * @param title
     * @param message
     * @param labelActionPosition
     */
    public static AlertDialog messageBox(Activity context, String title,
                                         String message, String labelActionPosition) {
        return messageBox(context, 0, title, message, labelActionPosition);
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param labelActionPosition
     */
    public static AlertDialog messageBox(Activity context, int iconId, String title,
                                         String message, String labelActionPosition) {
        try {
            if (ActivityUtils.isFinish(context)) return null;

            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(labelActionPosition, (dialog, which) -> dialog.dismiss())
                    .setCancelable(false);

            // set icon notification
            if (iconId > 0) builder.setIcon(iconId);

            return builder.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param labelActionPosition
     * @param callbackActionPosition
     */
    public static AlertDialog messageBox(Activity context, String title,
                                         String message, String labelActionPosition,
                                         OnClickListener callbackActionPosition) {
        return messageBox(context, 0, title, message, labelActionPosition, callbackActionPosition);
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param labelActionPosition
     * @param callbackActionPosition
     */
    public static AlertDialog messageBox(Activity context, int iconId, String title,
                                         String message, String labelActionPosition,
                                         OnClickListener callbackActionPosition) {
        try {
            if (ActivityUtils.isFinish(context)) return null;

            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(labelActionPosition, callbackActionPosition)
                    .setCancelable(false);

            // set icon notification
            if (iconId > 0) builder.setIcon(iconId);

            return builder.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @param context
     * @param iconId
     * @param title
     * @param message
     * @param labelActionPosition
     * @param labelActionNavigation
     * @param callbackActionPosition
     * @param callbackActionNavigation
     * @return
     */
    public static AlertDialog messageBox(Activity context, int iconId, String title,
                                         String message, String labelActionPosition, String labelActionNavigation,
                                         OnClickListener callbackActionPosition, OnClickListener callbackActionNavigation) {
        try {
            if (ActivityUtils.isFinish(context)) return null;

            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(labelActionPosition, callbackActionPosition)
                    .setNegativeButton(labelActionNavigation, callbackActionNavigation)
                    .setCancelable(false);

            // set icon notification
            if (iconId > 0) builder.setIcon(iconId);

            return builder.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    public static AlertDialog customBox(FragmentActivity fragmentActivity, View view) {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity)
                    .setView(view)
                    .setCancelable(true);

            return builder.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }


    /**
     * @param context
     * @param title
     * @param message
     * @param labelActionPosition
     * @param labelActionCancel
     * @param callbackActionPosition
     */
    public static AlertDialog confirmBox(Activity context, String title,
                                         String message, String labelActionPosition,
                                         String labelActionCancel,
                                         OnClickListener callbackActionPosition) {
        return confirmBox(context, 0, title, message, labelActionPosition, labelActionCancel, callbackActionPosition);
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param labelActionPosition
     * @param labelActionCancel
     * @param callbackActionPosition
     */
    public static AlertDialog confirmBox(Activity context, int iconId, String title,
                                         String message, String labelActionPosition,
                                         String labelActionCancel,
                                         OnClickListener callbackActionPosition) {
        try {
            if (ActivityUtils.isFinish(context)) return null;

            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(labelActionPosition, callbackActionPosition)
                    .setNegativeButton(labelActionCancel, (dialog, which) -> dialog.dismiss())
                    .setCancelable(false);

            // set icon notification
            if (iconId > 0) builder.setIcon(iconId);

            return builder.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param labelActionPosition
     * @param labelActionNegative
     * @param callback
     * @param cancel
     */
    public static AlertDialog confirmBox(Activity context, String title,
                                         String message, String labelActionPosition,
                                         String labelActionNegative,
                                         OnClickListener callback,
                                         OnClickListener cancel) {
        return confirmBox(context, 0, title, message, labelActionPosition, labelActionNegative, callback, cancel);
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param labelActionPosition
     * @param labelActionNegative
     * @param callback
     * @param cancel
     */
    public static AlertDialog confirmBox(Activity context, int iconId, String title,
                                         String message, String labelActionPosition,
                                         String labelActionNegative,
                                         OnClickListener callback,
                                         OnClickListener cancel) {
        try {
            if (ActivityUtils.isFinish(context)) return null;

            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(title).setMessage(message)
                    .setPositiveButton(labelActionPosition, callback)
                    .setNegativeButton(labelActionNegative, cancel)
                    .setCancelable(false);

            // set icon notification
            if (iconId > 0) builder.setIcon(iconId);

            return builder.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }
}
