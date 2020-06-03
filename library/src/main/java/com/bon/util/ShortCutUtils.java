package com.bon.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.bon.logger.Logger;

/**
 * Created by Dang on 11/25/2015.
 */
public class ShortCutUtils {
    private static final String TAG = ShortCutUtils.class.getSimpleName();

    /**
     * check shortcut has existed on screen
     *
     * @param activity
     * @return
     */
    public static boolean hasShortcut(Activity activity, String appName) {
        boolean isInstallShortcut = false;

        try {
            final ContentResolver cr = activity.getContentResolver();
            final String AUTHORITY = "com.android.launcher.settings";
            final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");

            Cursor c = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?", new String[]{appName.trim()}, null);
            if (c != null && c.getCount() > 0) {
                isInstallShortcut = true;
            }

            if (c != null) {
                c.close();
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return isInstallShortcut;
    }

    /**
     * add shortcut on screen
     *
     * @param activity
     * @param appName
     * @param iconLauncher
     */
    public static void addShortcut(Activity activity, String appName, int iconLauncher) {
        try {
            Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
            shortcut.putExtra("duplicate", false);
            Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
            shortcutIntent.setClassName(activity, activity.getClass().getName());
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

            Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(activity, iconLauncher);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
            activity.sendBroadcast(shortcut);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    /**
     * delete shortcut
     *
     * @param activity
     * @param appName
     */
    public static void delShortcut(Activity activity, String appName) {
        try {
            Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
            String appClass = activity.getPackageName() + "." + activity.getLocalClassName();
            ComponentName comp = new ComponentName(activity.getPackageName(), appClass);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
            activity.sendBroadcast(shortcut);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }
}
