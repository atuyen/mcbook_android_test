package com.mc.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.plus.PlusShare;


public class SocialUtils {

    //public static final String GOOGLE_PLAY_STORE_URI = "http://play.google.com/store/apps/details?id=";
    public static final String FACEBOOK_MESSENGER_PACKAGE = "com.facebook.orca";
    //public static final String APP_PACKAGE = "com.mc.books";

    public static void shareFacebookSocial(Context context, String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Check this app on: " + url);
        sendIntent.setType("text/plain");
        sendIntent.setPackage(FACEBOOK_MESSENGER_PACKAGE);
        try {
            context.startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Please install facebook messenger.", Toast.LENGTH_LONG).show();
        }
    }

    public static void shareGPlusSocial(Context context, String url, String title) {
        Intent shareIntent = new PlusShare.Builder(context)
                .setType("text/plain")
                .setText(title)
                .setContentUrl(Uri.parse("Check this app on: "
                        + url))
                .getIntent();
        try {
            ((Activity) context).startActivityForResult(shareIntent, 0);
        } catch (Exception e) {
            Toast.makeText(context, "No such method", Toast.LENGTH_LONG).show();
        }
    }
}
