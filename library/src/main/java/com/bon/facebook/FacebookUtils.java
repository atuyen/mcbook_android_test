package com.bon.facebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("ALL")
public class FacebookUtils {
    /**
     * @param context
     * @return MD5
     */
    public static String getHashKey(Context context) {
        String result = "";
        // Add code to print out the key hash
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                result = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                System.out.println("HashKey = " + result);
                return result;
            }
        } catch (NameNotFoundException e) {
            System.out.println("NameNotFoundException:: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException:: " + e.getMessage());
        }

        return result;
    }
}
