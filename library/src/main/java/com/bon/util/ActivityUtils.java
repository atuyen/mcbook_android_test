package com.bon.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bon.library.R;
import com.bon.logger.Logger;

import java.util.List;

/**
 * Created by Dang on 5/11/2016.
 */
public final class ActivityUtils {
    private static final String TAG = ActivityUtils.class.getSimpleName();

    /**
     * @param activity
     * @return
     */
    public static boolean isFinish(Activity activity) {
        try {
            if (activity == null || activity.isFinishing()) {
                Log.d(TAG, "Activity is finished!");
                return true;
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return false;
    }

    /**
     * Intent intent = new Intent(mContext, Account.class);
     * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     * intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
     * mContext.startActivity(intent);
     * <p>
     * Util.animationActivity(mContext, false);
     *
     * @param activity
     */
    public static void animationActivity(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_open_right_to_left, R.anim.activity_close_right_to_left);
    }

    /**
     * @param packageName
     * @param className
     * @return
     */
    public static boolean isActivityExists(@NonNull final String packageName, @NonNull final String className) {
        try {
            Intent intent = new Intent();
            intent.setClassName(packageName, className);
            return !(ExtUtils.getApp().getPackageManager().resolveActivity(intent, 0) == null ||
                    intent.resolveActivity(ExtUtils.getApp().getPackageManager()) == null ||
                    ExtUtils.getApp().getPackageManager().queryIntentActivities(intent, 0).size() == 0);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return false;
    }

    /**
     * @param cls
     */
    public static void startActivity(@NonNull final Class<?> cls) {
        Context context = ExtUtils.getApp();
        startActivity(context, null, context.getPackageName(), cls.getName(), null);
    }

    /**
     * @param cls
     * @param options
     */
    public static void startActivity(@NonNull final Class<?> cls, @NonNull final Bundle options) {
        Context context = ExtUtils.getApp();
        startActivity(context, null, context.getPackageName(), cls.getName(), options);
    }

    /**
     * @param activity
     * @param cls
     */
    public static void startActivity(@NonNull final Activity activity, @NonNull final Class<?> cls) {
        startActivity(activity, null, activity.getPackageName(), cls.getName(), null);
    }

    /**
     * @param activity
     * @param cls
     * @param options
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final Class<?> cls,
                                     @NonNull final Bundle options) {
        startActivity(activity, null, activity.getPackageName(), cls.getName(), options);
    }

    /**
     * @param activity
     * @param cls
     * @param enterAnim
     * @param exitAnim
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final Class<?> cls,
                                     @AnimRes final int enterAnim,
                                     @AnimRes final int exitAnim) {
        startActivity(activity, null, activity.getPackageName(), cls.getName(), null);
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * @param extras
     * @param cls
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Class<?> cls) {
        Context context = ExtUtils.getApp();
        startActivity(context, extras, context.getPackageName(), cls.getName(), null);
    }

    /**
     * @param extras
     * @param cls
     * @param options
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Class<?> cls,
                                     @NonNull final Bundle options) {
        Context context = ExtUtils.getApp();
        startActivity(context, extras, context.getPackageName(), cls.getName(), options);
    }

    /**
     * @param extras
     * @param activity
     * @param cls
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final Class<?> cls) {
        startActivity(activity, extras, activity.getPackageName(), cls.getName(), null);
    }

    /**
     * @param extras
     * @param activity
     * @param cls
     * @param options
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final Class<?> cls,
                                     @NonNull final Bundle options) {
        startActivity(activity, extras, activity.getPackageName(), cls.getName(), options);
    }

    /**
     * @param extras
     * @param activity
     * @param cls
     * @param enterAnim
     * @param exitAnim
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final Class<?> cls,
                                     @AnimRes final int enterAnim,
                                     @AnimRes final int exitAnim) {
        try {
            startActivity(activity, extras, activity.getPackageName(), cls.getName(), null);
            activity.overridePendingTransition(enterAnim, exitAnim);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param pkg
     * @param cls
     */
    public static void startActivity(@NonNull final String pkg,
                                     @NonNull final String cls) {
        startActivity(ExtUtils.getApp(), null, pkg, cls, null);
    }

    /**
     * @param pkg
     * @param cls
     * @param options
     */
    public static void startActivity(@NonNull final String pkg,
                                     @NonNull final String cls,
                                     @NonNull final Bundle options) {
        startActivity(ExtUtils.getApp(), null, pkg, cls, options);
    }

    /**
     * @param activity
     * @param pkg
     * @param cls
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls) {
        startActivity(activity, null, pkg, cls, null);
    }

    /**
     * @param activity
     * @param pkg
     * @param cls
     * @param options
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls,
                                     @NonNull final Bundle options) {
        startActivity(activity, null, pkg, cls, options);
    }

    /**
     * @param activity
     * @param pkg
     * @param cls
     * @param enterAnim
     * @param exitAnim
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls,
                                     @AnimRes final int enterAnim,
                                     @AnimRes final int exitAnim) {
        try {
            startActivity(activity, null, pkg, cls, null);
            activity.overridePendingTransition(enterAnim, exitAnim);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param extras
     * @param pkg
     * @param cls
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final String pkg,
                                     @NonNull final String cls) {
        startActivity(ExtUtils.getApp(), extras, pkg, cls, null);
    }

    /**
     * @param extras
     * @param pkg
     * @param cls
     * @param options
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final String pkg,
                                     @NonNull final String cls,
                                     @NonNull final Bundle options) {
        startActivity(ExtUtils.getApp(), extras, pkg, cls, options);
    }

    /**
     * @param extras
     * @param activity
     * @param pkg
     * @param cls
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls) {
        startActivity(activity, extras, pkg, cls, null);
    }

    /**
     * @param extras
     * @param activity
     * @param pkg
     * @param cls
     * @param options
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls,
                                     @NonNull final Bundle options) {
        startActivity(activity, extras, pkg, cls, options);
    }

    /**
     * @param extras
     * @param activity
     * @param pkg
     * @param cls
     * @param enterAnim
     * @param exitAnim
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls,
                                     @AnimRes final int enterAnim,
                                     @AnimRes final int exitAnim) {
        try {
            startActivity(activity, extras, pkg, cls, null);
            activity.overridePendingTransition(enterAnim, exitAnim);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param context
     * @param extras
     * @param pkg
     * @param cls
     * @param options
     */
    private static void startActivity(final Context context,
                                      final Bundle extras,
                                      final String pkg,
                                      final String cls,
                                      final Bundle options) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (extras != null) intent.putExtras(extras);
            intent.setComponent(new ComponentName(pkg, cls));

            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                context.startActivity(intent, options);
            } else {
                context.startActivity(intent);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * @param packageName
     * @return
     */
    public static String getLauncherActivity(@NonNull final String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PackageManager pm = ExtUtils.getApp().getPackageManager();
            List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
            for (ResolveInfo aInfo : info) {
                if (aInfo.activityInfo.packageName.equals(packageName)) {
                    return aInfo.activityInfo.name;
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "no " + packageName;
    }


    /**
     * @return get top activity
     */
    public static Activity getTopActivity() {
        return ExtUtils.sTopActivity;
    }

    /**
     * finish all activities
     */
    public static void finishAllActivities() {
        try {
            List<Activity> activityList = ExtUtils.sActivityList;
            for (int i = activityList.size() - 1; i >= 0; --i) {
                activityList.get(i).finish();
                activityList.remove(i);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
