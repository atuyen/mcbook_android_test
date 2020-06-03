package com.bon.retrofit;

import java.io.IOException;

import okhttp3.Request;
import okio.Buffer;

/**
 * Created by Administrator on 19/01/2017.
 */

public class RetrofitUtils {
    /**
     * @param request
     * @return
     */
    public static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            if (copy != null && copy.body() != null) {
                final Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            }
        } catch (final IOException e) {
            return e.getMessage();
        }

        return "";
    }
}
