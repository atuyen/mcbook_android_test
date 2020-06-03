package com.bon.permission;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.DialogUtils;

/**
 * Created by Dang on 10/14/2015.
 */
public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();

    // code
    public static final int REQUEST_CODE_PERMISSION = 123;

    /**
     * request permission for android >= 6
     *
     * @param activity
     * @param requestCodePermission
     * @param permissions
     * @return
     */
    public static boolean requestPermission(Activity activity, int requestCodePermission, String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null && permissions != null) {
                if (!hasPermissions(activity, permissions)) {
                    ActivityCompat.requestPermissions(activity, permissions, requestCodePermission);
                    return true;
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return false;
    }

    public static boolean requestPermission(Fragment fragment, int requestCodePermission, String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && fragment != null && permissions != null) {
                if (!hasPermissions(fragment.getContext(), permissions)) {
                    fragment.requestPermissions(permissions, requestCodePermission);
                    return true;
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return false;
    }


    /**
     * check permission for app
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return true;
    }

    public static void confirmSettingPermission(Activity context, String permission, String messageConfirm, String buttonOk, String buttonCancel) {
        try {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    DialogUtils.confirmBox(context, context.getString(R.string.app_name), messageConfirm, buttonOk, buttonCancel, (dialogInterface, i) -> {
                        try {
                            dialogInterface.dismiss();
                            ActivityCompat.requestPermissions(context, new String[]{permission}, REQUEST_CODE_PERMISSION);
                        } catch (Exception e) {
                            Logger.e(TAG, e);
                        }
                    });
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(context, new String[]{permission}, REQUEST_CODE_PERMISSION);
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * go to setting app
     *
     * @param context
     */
    public static void goToSettingApplication(Context context) {
        try {
            //Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            context.startActivity(intent);
        }
    }
}
