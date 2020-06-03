package com.bon.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.bon.logger.Logger;
import com.bon.network.UrlUtils;

import java.io.File;

/**
 * Created by Dang on 11/25/2015.
 */
public final class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();

    /**
     * get version code of app
     *
     * @param context
     * @return
     */
    public static int getVerCode(Context context) {
        int verCode = -1;

        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(TAG, e);
        }

        return verCode;
    }

    /**
     * get version name of app
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";

        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(TAG, e);
        }

        return verName;
    }

    /**
     * get to play tore to rating...
     *
     * @param activity
     */
    public static void goPlayStoreApp(Activity activity) {
        final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException ex) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * @param context
     * @param uri
     */
    public static void openActionView(Context context, Uri uri) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param url
     * @return
     */
    public static Uri getUriFromUrl(String url) {
        Uri uri = null;

        try {
            if (UrlUtils.isNetworkUrl(url)) {
                uri = Uri.parse(url);
            } else {
                File file = new File(url);
                if (file.exists()) uri = Uri.fromFile(file);
            }

            if (uri != null) {
                Logger.e("GetUriFromUrl", "Uri:: " + uri.toString());
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return uri;
    }

    /**
     * @param context
     * @param url
     */
    public static void openVideo(Context context, String url) {
        try {
            Uri uri = getUriFromUrl(url);
            if (uri == null) return;
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setDataAndType(uri, "video/*");
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param context
     * @param url
     */
    public static void openImage(Context context, String url) {
        try {
            Uri uri = getUriFromUrl(url);
            if (uri == null) return;
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setDataAndType(uri, "image/*");
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param context
     * @param url
     */
    public static void openOther(Context context, String url) {
        try {
            Uri uri = getUriFromUrl(url);
            if (uri == null) return;
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
