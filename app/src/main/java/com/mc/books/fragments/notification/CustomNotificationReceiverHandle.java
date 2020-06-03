package com.mc.books.fragments.notification;

import android.content.Context;
import android.util.Log;

import com.bon.eventbus.IEvent;
import com.bon.eventbus.RxBus;
import com.bon.sharepreferences.AppPreferences;
import com.mc.application.AppContext;
import com.mc.events.NotificationEvent;
import com.mc.interactors.service.IApiService;
import com.mc.models.BaseResponse;
import com.mc.utilities.Constant;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class CustomNotificationReceiverHandle implements OneSignal.NotificationReceivedHandler {
    IApiService apiService;
    RxBus<IEvent> rxBus;

    public CustomNotificationReceiverHandle(IApiService apiService, RxBus<IEvent> rxBus) {
        this.apiService = apiService;
        this.rxBus = rxBus;
    }

    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        Log.e("notificationReceived", data.toString());
        if (data != null) {
            rxBus.send(new NotificationEvent(Constant.KEY_NOTIFICATION_EVENT));
        }
    }
}
