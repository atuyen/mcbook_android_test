package com.mc.books.fragments.notification;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mc.books.activity.MainActivity;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class CustomNotificationOpenedHandle implements OneSignal.NotificationOpenedHandler {

    private Context context;

    public CustomNotificationOpenedHandle(Context context) {
        this.context = context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        JSONObject data = result.notification.payload.additionalData;
        Log.e("notificationOpened", data.toString());
        if (data != null) {
            String type = data.optString("type");
            String action = data.optString("action");
            int bookId = data.optInt("objectId");
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("action", action);
            intent.putExtra("bookId", bookId);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }
}

