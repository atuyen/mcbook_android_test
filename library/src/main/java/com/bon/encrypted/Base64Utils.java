package com.bon.encrypted;

import com.google.common.io.BaseEncoding;
import com.bon.logger.Logger;

/**
 * Created by Dang on 4/4/2016.
 */
public class Base64Utils {
    private static final String TAG = Base64Utils.class.getSimpleName();

    /**
     * encode string to base64
     *
     * @param data
     * @return
     */
    public static String encode(String data) {
        try {
            return BaseEncoding.base64().encode(data.getBytes());
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    /**
     * decode base64 to string
     *
     * @param base64String
     * @return
     */
    public static String decode(String base64String) {
        try {
            byte[] contentInBytes = BaseEncoding.base64().decode(base64String);
            return new String(contentInBytes, "UTF-8");
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }
}

