package com.bon.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.bon.logger.Logger;

import java.util.Locale;

public class LanguageUtils {
    private static final String TAG = LanguageUtils.class.getSimpleName();

    /**
     * change language for app
     *
     * @param activity
     * @param languageCode
     */
    public static void configLanguage(Activity activity, String languageCode) {
        try {
            Resources res = activity.getResources();
            // Change locale settings in the app.
            Configuration conf = res.getConfiguration();
            conf.locale = new Locale(languageCode);
            res.updateConfiguration(conf, res.getDisplayMetrics());
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
