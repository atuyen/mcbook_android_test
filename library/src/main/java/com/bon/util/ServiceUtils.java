package com.bon.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import com.bon.logger.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dangpp on 8/24/2017.
 */

public final class ServiceUtils {
    private static final String TAG = ServiceUtils.class.getSimpleName();

    /**
     * @return
     */
    public static Set getAllRunningService() {
        ActivityManager activityManager = (ActivityManager) ExtUtils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> runningServiceInfos = activityManager.getRunningServices(0x7FFFFFFF);
        Set<String> names = new HashSet<>();
        if (runningServiceInfos == null || runningServiceInfos.size() == 0) return null;
        for (RunningServiceInfo aInfo : runningServiceInfos) {
            names.add(aInfo.service.getClassName());
        }

        return names;
    }

    /**
     * @param className
     */
    public static void startService(final String className) {
        try {
            startService(Class.forName(className));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param cls
     */
    public static void startService(final Class<?> cls) {
        try {
            Intent intent = new Intent(ExtUtils.getApp(), cls);
            ExtUtils.getApp().startService(intent);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param className
     * @return
     */
    public static boolean stopService(final String className) {
        try {
            return stopService(Class.forName(className));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return false;
    }

    /**
     * @param cls
     * @return
     */
    public static boolean stopService(final Class<?> cls) {
        Intent intent = new Intent(ExtUtils.getApp(), cls);
        return ExtUtils.getApp().stopService(intent);
    }

    /**
     * @param className
     * @param conn
     * @param flags     <ul>
     *                  <li>{@link Context#BIND_AUTO_CREATE}</li>
     *                  <li>{@link Context#BIND_DEBUG_UNBIND}</li>
     *                  <li>{@link Context#BIND_NOT_FOREGROUND}</li>
     *                  <li>{@link Context#BIND_ABOVE_CLIENT}</li>
     *                  <li>{@link Context#BIND_ALLOW_OOM_MANAGEMENT}</li>
     *                  <li>{@link Context#BIND_WAIVE_PRIORITY}</li>
     *                  </ul>
     */
    public static void bindService(final String className, final ServiceConnection conn, final int flags) {
        try {
            bindService(Class.forName(className), conn, flags);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param cls
     * @param conn
     * @param flags <ul>
     *              <li>{@link Context#BIND_AUTO_CREATE}</li>
     *              <li>{@link Context#BIND_DEBUG_UNBIND}</li>
     *              <li>{@link Context#BIND_NOT_FOREGROUND}</li>
     *              <li>{@link Context#BIND_ABOVE_CLIENT}</li>
     *              <li>{@link Context#BIND_ALLOW_OOM_MANAGEMENT}</li>
     *              <li>{@link Context#BIND_WAIVE_PRIORITY}</li>
     *              </ul>
     */
    public static void bindService(final Class<?> cls, final ServiceConnection conn, final int flags) {
        Intent intent = new Intent(ExtUtils.getApp(), cls);
        ExtUtils.getApp().bindService(intent, conn, flags);
    }

    /**
     * @param conn
     */
    public static void unbindService(final ServiceConnection conn) {
        ExtUtils.getApp().unbindService(conn);
    }

    /**
     * @param className
     * @return
     */
    public static boolean isServiceRunning(final String className) {
        try {
            ActivityManager activityManager = (ActivityManager) ExtUtils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningServiceInfo> info = activityManager.getRunningServices(0x7FFFFFFF);
            if (info == null || info.size() == 0) return false;
            for (RunningServiceInfo aInfo : info) {
                if (className.equals(aInfo.service.getClassName())) {
                    return true;
                }
            }
        } catch (SecurityException e) {
            Logger.e(TAG, e);
        }

        return false;
    }
}