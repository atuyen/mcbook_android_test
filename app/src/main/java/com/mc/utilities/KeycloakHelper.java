/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors
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

package com.mc.utilities;

import android.app.Activity;
import android.util.Log;

import com.mc.books.BuildConfig;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.jboss.aerogear.android.authorization.oauth2.OAuthWebViewDialog;
import org.jboss.aerogear.android.core.Callback;

import java.net.URL;

public class KeycloakHelper {
    //        private static final String SSO = "MCB686";
//    private static final String SSO = Constant.SERVER_LIVE ? "MCBOOK" : "MCB686";
//    private static final String SHOOT_SERVER_URL = "https://sso.mentor.vn";
    private static final String SHOOT_SERVER_URL = BuildConfig.SSO_BASE_URL;
    private static final String AUTHZ_URL = SHOOT_SERVER_URL + "/auth";
    private static final String AUTHZ_ENDPOINT = "/realms/" + BuildConfig.SSO + "/protocol/openid-connect/auth";
    private static final String ACCESS_TOKEN_ENDPOINT = "/realms/" + BuildConfig.SSO + "/protocol/openid-connect/token";
    private static final String REFRESH_TOKEN_ENDPOINT = "/realms/" + BuildConfig.SSO + "/protocol/openid-connect/token";
    private static final String AUTHZ_ACCOOUNT_ID = "mobile";
    private static final String AUTHZ_CLIENT_ID = "mobile";
    private static final String AUTHZ_REDIRECT_URL = "com.mc.books://oauth2Callback";
    private static final String MODULE_NAME = "mobile";

//    static {
//        try {
//            AuthorizationManager.config(MODULE_NAME, OAuth2AuthorizationConfiguration.class)
//                    .setBaseURL(new URL(AUTHZ_URL))
//                    .setAuthzEndpoint(AUTHZ_ENDPOINT)
//                    .setAccessTokenEndpoint(ACCESS_TOKEN_ENDPOINT)
//                    .setRefreshEndpoint(REFRESH_TOKEN_ENDPOINT)
//                    .setAccountId(AUTHZ_ACCOOUNT_ID)
//                    .setClientId(AUTHZ_CLIENT_ID)
//                    .setRedirectURL(AUTHZ_REDIRECT_URL)
//                    .setWithIntent(true)
//                    .asModule();
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static void connect(final Activity activity, final Callback callback) {
        try {
            AuthorizationManager.config(MODULE_NAME, OAuth2AuthorizationConfiguration.class)
                    .setBaseURL(new URL(AUTHZ_URL))
                    .setAuthzEndpoint(AUTHZ_ENDPOINT)
                    .setAccessTokenEndpoint(ACCESS_TOKEN_ENDPOINT)
                    .setRefreshEndpoint(REFRESH_TOKEN_ENDPOINT)
                    .setAccountId(AUTHZ_ACCOOUNT_ID)
                    .setClientId(AUTHZ_CLIENT_ID)
                    .setRedirectURL(AUTHZ_REDIRECT_URL)
                    .setWithIntent(true)
                    .asModule();

            final AuthzModule authzModule = AuthorizationManager.getModule(MODULE_NAME);

            authzModule.requestAccess(activity, new Callback<String>() {
                @SuppressWarnings("unchecked")
                @Override
                public void onSuccess(String s) {
                    callback.onSuccess(s);
                    authzModule.unbindService();
                    AuthorizationManager.clearModule();

                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("onFailure", e.toString());
                    if (!e.getMessage().matches(OAuthWebViewDialog.OAuthReceiver.DISMISS_ERROR)) {
                        authzModule.deleteAccount();
                    }
                    callback.onFailure(e);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static boolean isConnected() {
        return AuthorizationManager.getModule(MODULE_NAME).isAuthorized();
    }

}
