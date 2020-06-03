package com.bon.cloudmessage;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Dang Pham Phu on 2/26/2017.
 */

public class PlayServiceUtils {
    /**
     * @param context
     * @return
     */
    public static boolean isPlayServices(Context context) {
        try {
            GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
            int result = googleAPI.isGooglePlayServicesAvailable(context);
            return result == ConnectionResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
