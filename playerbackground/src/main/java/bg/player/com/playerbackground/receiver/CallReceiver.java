package bg.player.com.playerbackground.receiver;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import bg.player.com.playerbackground.event.MessageEvent;
import bg.player.com.playerbackground.receiver.PhonecallReceiver;

public class CallReceiver extends PhonecallReceiver {

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
        Log.e("onIncomingCallReceived", "onIncomingCallReceived");
        EventBus.getDefault().post(new MessageEvent("onIncomingCallReceived"));
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        Log.e("onIncomingCallAnswered", "onIncomingCallAnswered");
        EventBus.getDefault().post(new MessageEvent("onIncomingCallAnswered"));
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.e("onIncomingCallEnded", "onIncomingCallEnded");
        EventBus.getDefault().post(new MessageEvent("onIncomingCallEnded"));
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        Log.e("onOutgoingCallStarted", "onOutgoingCallStarted");
        EventBus.getDefault().post(new MessageEvent("onOutgoingCallStarted"));
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.e("onOutgoingCallEnded", "onOutgoingCallEnded");
        EventBus.getDefault().post(new MessageEvent("onOutgoingCallEnded"));
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        Log.e("onMissedCall", "onMissedCall");
        EventBus.getDefault().post(new MessageEvent("onMissedCall"));
    }

}
