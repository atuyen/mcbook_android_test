package com.bon.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;

import com.bon.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import java8.util.function.Consumer;
import rx.functions.Action0;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    /**
     * check network is variable
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return false;
    }

    /**
     * check network is variable
     *
     * @param context
     * @param action
     */
    public static void isNetworkAvailable(Context context, Action0 action) {
        if (isNetworkAvailable(context) && action != null) {
            action.call();
        }
    }


    /**
     * get type of network
     *
     * @param context
     * @return 2G, 3G, 4G, Unknown
     */
    public static String getTypeOfNetWork(Context context) {
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            int networkType = mTelephonyManager.getNetworkType();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "4G";
                default:
                    return "Unknown";
            }
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private static final String IPV4_BASIC_PATTERN_STRING =
            "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" + // initial 3 fields, 0-255 followed by .
                    "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])"; // final field, 0-255

    private static final Pattern IPV4_PATTERN = Pattern.compile("^" + IPV4_BASIC_PATTERN_STRING + "$");

    /**
     * @param input
     * @return
     */
    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : networkInterfaces) {
                List<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress inetAddress : inetAddresses) {
                    if (!inetAddress.isLoopbackAddress()) {
                        String sAddr = inetAddress.getHostAddress().toUpperCase();
                        boolean isIPv4 = isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4) {
                                return sAddr;
                            }
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return "";
    }

    /**
     * @param ipAddressConsumer
     */
    public static void getIpAddressInternet(Context context, Consumer<String> ipAddressConsumer) {
        try {
            // check network
            if (!NetworkUtils.isNetworkAvailable(context)) return;

            // request ip address from internet
            AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                protected String doInBackground(Void... voids) {
                    try (java.util.Scanner scanner = new java.util.Scanner(new java.net.URL("https://api.ipify.org/?format=json").openStream(), "UTF-8").useDelimiter("\\A")) {
                        JSONObject jsonObject = new JSONObject(scanner.next());
                        return jsonObject.getString("ip");
                    } catch (java.io.IOException e) {
                        Logger.e(TAG, e);
                    } catch (JSONException e) {
                        Logger.e(TAG, e);
                    }

                    return "";
                }

                @Override
                protected void onPostExecute(String ipAddress) {
                    super.onPostExecute(ipAddress);
                    if (ipAddressConsumer != null) {
                        ipAddressConsumer.accept(ipAddress);
                    }
                }
            };

            // execute
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                asyncTask.execute();
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public static void setStrictMode() {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
