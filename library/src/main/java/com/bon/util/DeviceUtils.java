package com.bon.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.bon.logger.Logger;

@SuppressWarnings("ALL")
public class DeviceUtils {
    private static final String TAG = DeviceUtils.class.getSimpleName();

    // const
    private static final float ALPHA_DIM_VALUE = 0.1f;

    public static String getDeviceInfo() {
        try {
            return "Model: " + Build.MODEL
                    + "\nId: " + Build.ID
                    + "\nDevice: " + Build.DEVICE
                    + "\nBrand: " + Build.BRAND
                    + "\nDisplay: " + Build.DISPLAY
                    + "\nHardware: " + Build.HARDWARE
                    + "\nBoard: " + Build.BOARD
                    + "\nHost: " + Build.HOST
                    + "\nManufacturer: " + Build.MANUFACTURER
                    + "\nProduct: " + Build.PRODUCT;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }

    public static String getDeviceOs() {
        return "Android " + Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
    }

    /**
     * get device id
     *
     * @param act
     * @return device id
     */
    public static String getDeviceId(Context act) {
        String deviceId = "";

        try {
            deviceId = android.provider.Settings.Secure.getString(act.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            if (TextUtils.isEmpty(deviceId)) {
                TelephonyManager tm = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
                deviceId = tm.getDeviceId();

                if (TextUtils.isEmpty(deviceId)) {
                    deviceId = tm.getSimSerialNumber();
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return deviceId;
    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        try {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;

            if (model.startsWith(manufacturer)) {
                return capitalize(model);
            }

            return capitalize(manufacturer) + " " + model;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }

    private static String capitalize(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return str;
            }

            String phrase = "";

            char[] arr = str.toCharArray();
            boolean capitalizeNext = true;
            for (char character : arr) {
                if (capitalizeNext && Character.isLetter(character)) {
                    phrase += Character.toUpperCase(character);
                    capitalizeNext = false;
                    continue;
                } else if (Character.isWhitespace(character)) {
                    capitalizeNext = true;
                }

                phrase += character;
            }

            return phrase;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }

    /**
     * check device support camera
     *
     * @param context
     * @return
     */
    public static boolean isDeviceSupportCamera(Context context) {
        try {
            if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                return true;
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return false;
    }

    /**
     * don't rotate screen
     *
     * @param activity
     */
    public static void lockScreenOrientation(Activity activity) {
        try {
            int currentOrientation = activity.getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param activity
     */
    public static void unlockScreenOrientation(Activity activity) {
        try {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @return isHoneycombOrAbove
     */
    public static boolean isHoneycombOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * @param view
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void dimView(View view) {
        try {
            if (isHoneycombOrAbove()) {
                view.setAlpha(ALPHA_DIM_VALUE);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param alpha
     * @param views
     */
    public static void setAlpha(float alpha, View... views) {
        try {
            if (isHoneycombOrAbove()) {
                for (View view : views) {
                    view.setAlpha(alpha);
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
