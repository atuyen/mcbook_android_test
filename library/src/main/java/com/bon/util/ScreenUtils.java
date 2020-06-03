package com.bon.util;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bon.logger.Logger;

/**
 * Created by dangpp on 8/24/2017.
 */

public final class ScreenUtils {
    private static final String TAG = ScreenUtils.class.getSimpleName();

    /**
     * @return
     */
    public static int getScreenWidth() {
        return ExtUtils.getApp().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * @return
     */
    public static int getScreenHeight() {
        return ExtUtils.getApp().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * @param activity
     */
    public static void setFullScreen(@NonNull final Activity activity) {
        try {
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param activity
     */
    public static void setLandscape(@NonNull final Activity activity) {
        try {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param activity
     */
    public static void setPortrait(@NonNull final Activity activity) {
        try {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @return
     */
    public static boolean isLandscape() {
        return ExtUtils.getApp().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * @return
     */
    public static boolean isPortrait() {
        return ExtUtils.getApp().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * @param activity
     * @return
     */
    public static int getScreenRotation(@NonNull final Activity activity) {
        try {
            switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
                default:
                case Surface.ROTATION_0:
                    return 0;
                case Surface.ROTATION_90:
                    return 90;
                case Surface.ROTATION_180:
                    return 180;
                case Surface.ROTATION_270:
                    return 270;
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return 0;
    }

    /**
     * @param activity
     * @return
     */
    public static Bitmap screenShot(@NonNull final Activity activity) {
        return screenShot(activity, true);
    }

    /**
     * @param activity
     * @param isDeleteStatusBar
     * @return
     */
    public static Bitmap screenShot(@NonNull final Activity activity, boolean isDeleteStatusBar) {
        try {
            View decorView = activity.getWindow().getDecorView();
            decorView.setDrawingCacheEnabled(true);
            decorView.buildDrawingCache();

            Bitmap bmp = decorView.getDrawingCache();
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

            Bitmap ret;
            if (isDeleteStatusBar) {
                Resources resources = activity.getResources();
                int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
                int statusBarHeight = resources.getDimensionPixelSize(resourceId);
                ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight);
            } else {
                ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
            }
            decorView.destroyDrawingCache();

            return ret;
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @return
     */
    public static boolean isScreenLock() {
        KeyguardManager km = (KeyguardManager) ExtUtils.getApp().getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    /**
     * @param duration
     */
    public static void setSleepDuration(final int duration) {
        Settings.System.putInt(ExtUtils.getApp().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, duration);
    }

    /**
     * @return
     */
    public static int getSleepDuration() {
        try {
            return Settings.System.getInt(ExtUtils.getApp().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Settings.SettingNotFoundException e) {
            Logger.e(TAG, e);
        }

        return 0;
    }
}