package com.bon.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.bon.logger.Logger;

import java.util.LinkedList;
import java.util.List;

import java8.util.function.Consumer;

/**
 * Created by dangpp on 8/24/2017.
 */

public final class ExtUtils {
    private static final String TAG = ExtUtils.class.getSimpleName();

    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    static List<Activity> sActivityList = new LinkedList<>();
    @SuppressLint("StaticFieldLeak")
    static Activity sTopActivity;

    private static class AppActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        private Consumer<String> lifecycleConsumer;

        public AppActivityLifecycleCallbacks(Consumer<String> lifecycleConsumer) {
            this.lifecycleConsumer = lifecycleConsumer;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            try {
                sActivityList.add(activity);
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            try {
                sTopActivity = activity;
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (lifecycleConsumer != null) lifecycleConsumer.accept("");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            try {
                sActivityList.remove(activity);
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }
    }


    /**
     * @param app
     */
    public static void init(@NonNull final Application app, Consumer<String> lifecycleConsumer) {
        ExtUtils.sApplication = app;
        app.registerActivityLifecycleCallbacks(new AppActivityLifecycleCallbacks(lifecycleConsumer));
    }

    /**
     * @return
     */
    public static Application getApp() {
        if (sApplication != null) return sApplication;
        throw new NullPointerException("You should init first!!!");
    }
}