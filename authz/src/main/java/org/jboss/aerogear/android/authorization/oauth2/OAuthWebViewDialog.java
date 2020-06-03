/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.android.authorization.oauth2;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.jboss.aerogear.android.authorization.R;

import java.net.URL;

/**
 * This is a WebView Dialog which is opened to a OAuth2 sign in page and sends
 * data back to the calling activity.
 */
public class OAuthWebViewDialog extends DialogFragment {

    private static final String TAG = OAuthWebViewDialog.class.getSimpleName();
    private static final String TITLE = "org.jboss.aerogear.android.authorize.OAuthWebViewDialog.TITLE";
    private static final String AUTHORIZE_URL = "org.jboss.aerogear.android.authorize.OAuthWebViewDialog.AUTHORIZE_URL";
    private static final String REDIRECT_URL = "org.jboss.aerogear.android.authorize.OAuthWebViewDialog.REDIRECT_URL";
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 7.0; Android SDK built for x86 Build/NYC; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/65.0.3325.109 Mobile Safari/537.36";


    private WebView webView;
    private ProgressBar progressBar;
    private String authorizeUrl;

    final private OAuthViewClient client = new OAuthViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }
    };

    private String redirectURL;

    private class OAuthViewClient extends WebViewClient {
        private OAuthReceiver receiver;
        private String redirectURL;



        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.startsWith(redirectURL)) {
                if (url.contains("code=")) {
                    prefsEditor.putString("REDIRECT_URL", url);
                    prefsEditor.commit();

                    final String token = fetchToken(url);
                    Log.d("TOKEN", token);
                    if (receiver != null) {
                        final OAuthReceiver receiverRef = receiver;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                receiverRef.receiveOAuthCode(token);
                            }
                        });
                    }
                    return;
                } else if (url.contains("error=")) {
                    final String error = fetchError(url);
                    Log.d("ERROR", error);
                    if (receiver != null) {
                        final OAuthReceiver receiverRef = receiver;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                receiverRef.receiveOAuthError(error);
                            }
                        });
                    }
                    return;
                }
            }

            super.onPageFinished(view, url);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("HIEPUH", webView.getSettings().getUserAgentString());
            if (url.startsWith(redirectURL)) {
                if (url.contains("code=")) {
                    prefsEditor.putString("REDIRECT_URL", url);
                    prefsEditor.commit();

                    final String token = fetchToken(url);
                    Log.d("TOKEN", token);
                    if (receiver != null) {
                        final OAuthReceiver receiverRef = receiver;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                receiverRef.receiveOAuthCode(token);
                            }
                        });
                    }
                    return true;
                } else if (url.contains("error=")) {
                    final String error = fetchError(url);
                    Log.d("ERROR", error);
                    if (receiver != null) {
                        final OAuthReceiver receiverRef = receiver;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                receiverRef.receiveOAuthError(error);
                            }
                        });
                    }
                    return true;
                }
            }
            return false;

        }

        private String fetchToken(String url) {
            return fetchURLParam(url, "code");
        }

        private String fetchError(String url) {
            return fetchURLParam(url, "error");
        }

        private String fetchURLParam(String url, String param) {
            Uri uri = Uri.parse(url);
            return uri.getQueryParameter(param);
        }
    }

    public static OAuthWebViewDialog newInstance(URL authorizeURL, Uri redirectURL) {
        OAuthWebViewDialog instance = new OAuthWebViewDialog();
        instance.authorizeUrl = authorizeURL.toString();
        instance.redirectURL = redirectURL.toString();

        Bundle args = new Bundle();
        args.putString(AUTHORIZE_URL, instance.authorizeUrl);
        args.putString(REDIRECT_URL, instance.redirectURL);
        instance.setArguments(args);
        return instance;
    }

    SharedPreferences appSharedPrefs;
    SharedPreferences.Editor prefsEditor;

    @Override
    public void onViewCreated(View arg0, Bundle arg1) {
        super.onViewCreated(arg0, arg1);

        client.redirectURL = redirectURL;

        webView.loadUrl(authorizeUrl);
        webView.setWebViewClient(client);

        // activates JavaScript (just in case)
        WebSettings webSettings = webView.getSettings();
//        webSettings.setUserAgentString(USER_AGENT);
        webSettings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
        }

        appSharedPrefs = getActivity().getApplicationContext().getSharedPreferences(getActivity().getApplicationContext().getPackageName(),
                Context.MODE_PRIVATE);
        prefsEditor = appSharedPrefs.edit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.authorizeUrl = getArguments().getString(AUTHORIZE_URL);
        this.redirectURL = getArguments().getString(REDIRECT_URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.oauth_web_view, container, false);

        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);

        webView = (WebView) v.findViewById(R.id.web_oauth);
        webView.setScrollContainer(true);
        getDialog().getWindow().setTitle("OAuth 2 Dialog");
        return v;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (client.receiver != null) {
            client.receiver.receiveOAuthError(OAuthReceiver.DISMISS_ERROR);
        }
    }

    public void setReceiver(OAuthReceiver receiver) {
        client.receiver = receiver;
    }

    public void removeReceive() {
        client.receiver = null;
    }

    public interface OAuthReceiver {

        public static final String DISMISS_ERROR = "dialog_dismissed";

        void receiveOAuthCode(String code);

        public void receiveOAuthError(String error);
    }

}
