package com.mc.interactors.service;

import android.content.Context;

import com.bon.sharepreferences.AppPreferences;
import com.mc.application.AppContext;
import com.mc.utilities.Constant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dangpp on 2/9/2018.
 */

public class AccessInterceptor implements Interceptor {
    private final AppPreferences appPreferences;

    public AccessInterceptor(Context context) {
        this.appPreferences = AppPreferences.getInstance(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = "";

        Request request;
        if (token != null) {
            request = chain.request().newBuilder()
                    .addHeader(Constant.AUTHORIZATION, AppContext.getTokenApp())
                    .addHeader("partition", "mcbooks")
                    .build();
        } else {
            request = chain.request();
        }

        return chain.proceed(request);
    }
}