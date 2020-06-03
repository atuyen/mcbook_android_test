package com.bon.cloudmessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.bon.library.R;
import com.bon.util.StringUtils;

/**
 * Created by Dang Pham Phu on 2/26/2017.
 */

public class GcmNotification {
    // instance
    private static GcmNotification gcmNotification = null;

    // variable
    private Context context;
    private NotificationManager notificationManager;

    /**
     * instance
     *
     * @param context
     * @return
     */
    public static GcmNotification getInstance(Context context) {
        if (gcmNotification == null) {
            synchronized (GcmNotification.class) {
                if (gcmNotification == null) {
                    gcmNotification = new GcmNotification(context);
                }
            }
        }

        return gcmNotification;
    }

    /**
     * @param context
     */
    public GcmNotification(Context context) {
        try {
            this.context = context;
            this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * clear notification
     */
    public void clearNotification() {
        try {
            this.notificationManager.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * @param pendingIntent  : Intent used on the click of the notification :
     *                       Here is an example
     *                       Intent intent = new Intent(this, Activity.class);
     *                       if (screenRedirection != null && redirectParam != null) {
     *                       intent.putExtra(EXTRA_SCREEN_REDIRECTION, screenRedirection);
     *                       intent.putExtra(EXTRA_SCREEN_REDIRECTION_PARAM, redirectParam);
     *                       }
     *                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
     *                       <p>
     *                       PendingIntent pendingIntent = PendingIntent.getActivity(context, RESULT CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
     * @param title          , title of the notification
     * @param smallIconResId , the right icon visible
     * @param largeIconResId , the left icon visible
     * @param message        , message displayed to the user
     * @param summaryText    ,
     * @param autoCancel     , true to cancel on click
     * @return a notification to show
     */
    public Notification generate(final PendingIntent pendingIntent, @DrawableRes int smallIconResId,
                                 @DrawableRes int largeIconResId,
                                 final String title, @Nullable final String message,
                                 @Nullable final String summaryText, boolean autoCancel) {
        try {
            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

            // message
            if (!StringUtils.isEmpty(message)) bigText.bigText(message);

            // title
            bigText.setBigContentTitle(title);

            // summaryText
            if (!StringUtils.isEmpty(summaryText)) bigText.setSummaryText(summaryText);

            // resourceIcon
            Bitmap bigIcon = BitmapFactory.decodeResource(context.getResources(), largeIconResId);

            // builder
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setContentIntent(pendingIntent)
                    .setSmallIcon(smallIconResId)
                    .setLargeIcon(bigIcon)
                    .setContentTitle(title).setContentText(message).setTicker(title).setStyle(bigText)
                    .setDefaults(Notification.DEFAULT_ALL).setAutoCancel(autoCancel);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setColor(context.getResources().getColor(R.color.colorPrimary));
            }

            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /***
     * Show a notification on the device
     *
     * @param id
     * @param notification
     */
    public void show(int id, Notification notification) {
        try {
            this.notificationManager.notify(id, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}