package com.bon.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.telephony.SmsManager;

import com.bon.logger.Logger;

import java.io.File;

import java8.util.function.Consumer;

/**
 * Created by Dang on 10/20/2015.
 */
public class SharedUtils {
    private static final String TAG = SharedUtils.class.getSimpleName();

    // const
    public static final String TYPE_TEXT = "text/plain";
    public static final String TYPE_IMAGE = "image/*";

    /**
     * @param context
     * @param title
     * @param type:       SharedUtils.TYPE_TEXT || SharedUtils.TYPE_IMAGE
     * @param subject
     * @param content
     * @param file
     * @param packageName
     */
    public static void actionShare(Context context, String title, String type, String to, String subject,
                                   String content, File file, String packageName) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType(type);

            // to
            if (!StringUtils.isEmpty(to)) {
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
            }

            // subject
            if (!StringUtils.isEmpty(subject)) {
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            }

            // content
            if (!StringUtils.isEmpty(content)) {
                intent.putExtra(Intent.EXTRA_TEXT, content);
            }

            // file
            if (file != null && file.exists()) {
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            }

            // package name
            if (!StringUtils.isEmpty(packageName)) {
                intent.setPackage(packageName);
                context.startActivity(intent);
            } else {
                context.startActivity(Intent.createChooser(intent, title));
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    /**
     * share content to sms
     *
     * @param context
     * @param content
     */
    public static void actionSendSms(Context context, String content) {
        try {
            if (StringUtils.isEmpty(content)) return;

            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("sms_body", content);
            sendIntent.setType("vnd.android-dir/mms-sms");
            context.startActivity(sendIntent);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    //---sends an SMS message to another device---
    public static void actionSendSms(Context context, String phoneNumber, String message, Consumer<Boolean> consumer) {
        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);

            //---when the SMS has been sent---
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            System.out.println("SendSMS:: SMS sent");
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            if (consumer != null) {
                                consumer.accept(false);
                            }
                            System.out.println("SendSMS:: Generic failure");
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            if (consumer != null) {
                                consumer.accept(false);
                            }
                            System.out.println("SendSMS:: No service");
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            if (consumer != null) {
                                consumer.accept(false);
                            }
                            System.out.println("SendSMS:: Null PDU");
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            if (consumer != null) {
                                consumer.accept(false);
                            }
                            System.out.println("SendSMS:: Radio off");
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            //---when the SMS has been delivered---
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            if (consumer != null) {
                                consumer.accept(true);
                            }
                            System.out.println("ReceiveSMS:: SMS delivered");
                            break;
                        case Activity.RESULT_CANCELED:
                            if (consumer != null) {
                                consumer.accept(false);
                            }
                            System.out.println("ReceiveSMS:: SMS not delivered");
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
