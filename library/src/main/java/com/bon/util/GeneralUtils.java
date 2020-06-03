package com.bon.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;

import com.bon.logger.Logger;

public class GeneralUtils {
    private static final String TAG = GeneralUtils.class.getSimpleName();

    /**
     * convert dp to pixel
     *
     * @param context
     * @param dimenId
     * @return Pixel
     */
    public static int convertDpMeasureToPixel(Context context, int dimenId) {
        int px = 0;

        try {
            Resources resources = context.getResources();
            px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, resources.getDimension(dimenId), resources.getDisplayMetrics());
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return px;
    }

    /**
     * convert sp to pixel
     *
     * @param context
     * @param dimenId
     * @return
     */
    public static int convertSpMeasureToPixel(Context context, int dimenId) {
        int px = 0;

        try {
            Resources resources = context.getResources();
            px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(dimenId), resources.getDisplayMetrics());
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return px;
    }

    /**
     * the check device is tablet or phone
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        try {
            return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return false;
    }

    /**
     * @param context
     * @return DisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager windowManager;
        DisplayMetrics displayMetrics;

        try {
            displayMetrics = new DisplayMetrics();
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        } catch (Exception e) {
            Logger.e(TAG, e);
            displayMetrics = null;
        }

        return displayMetrics;
    }

    /**
     * @param context
     * @return Configuration.ORIENTATION_SQUARE,
     * Configuration.ORIENTATION_PORTRAIT,
     * Configuration.ORIENTATION_LANDSCAPE
     */
    @SuppressWarnings("deprecation")
    public static int getScreenOrientation(Context context) {
        int orientation = Configuration.ORIENTATION_UNDEFINED;

        try {
            DisplayMetrics metrics = getDisplayMetrics(context);
            if (metrics.widthPixels == metrics.heightPixels) {
                orientation = Configuration.ORIENTATION_SQUARE;
            } else {
                if (metrics.widthPixels < metrics.heightPixels) {
                    orientation = Configuration.ORIENTATION_PORTRAIT;
                } else {
                    orientation = Configuration.ORIENTATION_LANDSCAPE;
                }
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return orientation;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;

        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
                Log.e("result", "result:: " + result);
            }
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }

        return result;
    }

    public static int getNavigationBarHeight(Context context) {
        int result = 0;

        try {
            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
                Log.e("result", "result:: " + result);
            }
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }

        return result;
    }

    public static Drawable getDrawableFromResource(Context context, int resId) {
        try {
            return context.getResources().getDrawable(resId);
        } catch (Resources.NotFoundException ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    public static int getColorFromResource(Context context, int resId) {
        try {
            return context.getResources().getColor(resId);
        } catch (Resources.NotFoundException ex) {
            Logger.e(TAG, ex);
        }

        return 0;
    }
}
