package com.bon.network;

import android.annotation.SuppressLint;

import com.bon.logger.Logger;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TrustAllSSLCerts {
    protected static final String TAG = TrustAllSSLCerts.class.getSimpleName();

    /**
     * get ssl socket factory
     *
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;

        try {
            X509TrustManager tm = new X509TrustManager() {
                @SuppressLint("TrustAllX509TrustManager")
                public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string) throws CertificateException {
                }

                @SuppressLint("TrustAllX509TrustManager")
                public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string) throws CertificateException {
                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{tm}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            Logger.e(TAG, e);
        }

        return sslSocketFactory;
    }

    /**
     * ignore certificate network
     */
    public static void nuke() {
        try {
            HttpsURLConnection.setDefaultSSLSocketFactory(getSSLSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
} 