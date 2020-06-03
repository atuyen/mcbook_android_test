package com.bon.image;

import android.content.Context;

import com.nostra13.universalimageloader.core.assist.FlushedInputStream;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.bon.logger.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Dang on 5/27/2016.
 */
public class CustomizeImageDownloader extends BaseImageDownloader {
    private static final String TAG = CustomizeImageDownloader.class.getName();

    public CustomizeImageDownloader(Context context) {
        this(context, DEFAULT_HTTP_CONNECT_TIMEOUT, DEFAULT_HTTP_READ_TIMEOUT);
    }

    public CustomizeImageDownloader(Context context, int connectTimeout, int readTimeout) {
        super(context, connectTimeout, readTimeout);
    }

    @Override
    protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
        try {
            URL url = new URL(imageUri);
            HttpURLConnection http;

            if (Scheme.ofUri(imageUri) == Scheme.HTTPS) {
                http = (HttpsURLConnection) url.openConnection();
            } else {
                http = (HttpURLConnection) url.openConnection();
            }

            assert http != null;
            http.connect();
            http.setConnectTimeout(connectTimeout);
            http.setReadTimeout(readTimeout);

            return new FlushedInputStream(new BufferedInputStream(http.getInputStream()));
        } catch (MalformedURLException e) {
            Logger.e(TAG, e);
        }

        return null;
    }
}