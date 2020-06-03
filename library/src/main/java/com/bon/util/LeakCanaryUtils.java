package com.bon.util;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by dangpp on 4/12/2018.
 */

public class LeakCanaryUtils {
    /**
     * @param application
     */
    public static void init(Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis. You should not init your app in this process.
            return;
        }

        LeakCanary.install(application);
    }
}
