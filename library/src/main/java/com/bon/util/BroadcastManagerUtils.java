package com.bon.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.bon.logger.Logger;

/**
 * Created by Dang on 11/5/2015.
 */
public class BroadcastManagerUtils {
    private static final String TAG = BroadcastManagerUtils.class.getSimpleName();

    /**
     * register broadcast receiver
     *
     * @param context
     * @param broadcastReceiver
     * @param intentFilter
     */
    public static void registerReceiver(Context context, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        try {
            LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    /**
     * unregister broadcast receiver
     *
     * @param context
     * @param broadcastReceiver
     */
    public static void unregisterReceiver(Context context, BroadcastReceiver broadcastReceiver) {
        try {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    /**
     * send broad cast
     *
     * @param context
     * @param intent
     */
    public static void sendBroadcast(Context context, Intent intent) {
        try {
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }
}
