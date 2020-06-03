package com.bon.util;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Xml;

import com.bon.logger.Logger;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dang on 12/30/2015.
 */
public class PhoneUtils {
    private static final String TAG = PhoneUtils.class.getSimpleName();

    /**
     * @return
     */
    public static boolean isPhone() {
        TelephonyManager tm = (TelephonyManager) ExtUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}
     *
     * @return
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getIMEI() {
        TelephonyManager tm = (TelephonyManager) ExtUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getDeviceId() : null;
    }

    /**
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}
     *
     * @return
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getIMSI() {
        TelephonyManager tm = (TelephonyManager) ExtUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getSubscriberId() : null;
    }

    /**
     * <ul>
     * <li>{@link TelephonyManager#PHONE_TYPE_NONE } : 0</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_GSM  } : 1</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_CDMA } : 2</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_SIP  } : 3</li>
     * </ul>
     */
    public static int getPhoneType() {
        TelephonyManager tm = (TelephonyManager) ExtUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getPhoneType() : -1;
    }

    /**
     * @return
     */
    public static boolean isSimCardReady() {
        TelephonyManager tm = (TelephonyManager) ExtUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /**
     * @return
     */
    public static String getSimOperatorName() {
        TelephonyManager tm = (TelephonyManager) ExtUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getSimOperatorName() : null;
    }

    /**
     * <p>{@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * @return DeviceId(IMEI) = 99000311726612<br>
     * DeviceSoftwareVersion = 00<br>
     * Line1Number =<br>
     * NetworkCountryIso = cn<br>
     * NetworkOperator = 46003<br>
     * NetworkOperatorName<br>
     * NetworkType = 6<br>
     * honeType = 2<br>
     * SimCountryIso = cn<br>
     * SimOperator = 46003<br>
     * SimOperatorName<br>
     * SimSerialNumber = 89860315045710604022<br>
     * SimState = 5<br>
     * SubscriberId(IMSI) = 460030419724900<br>
     * VoiceMailNumber = *86<br>
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getPhoneStatus() {
        String str = "";

        try {
            TelephonyManager tm = (TelephonyManager) ExtUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
            str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
            str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
            str += "Line1Number = " + tm.getLine1Number() + "\n";
            str += "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
            str += "NetworkOperator = " + tm.getNetworkOperator() + "\n";
            str += "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
            str += "NetworkType = " + tm.getNetworkType() + "\n";
            str += "PhoneType = " + tm.getPhoneType() + "\n";
            str += "SimCountryIso = " + tm.getSimCountryIso() + "\n";
            str += "SimOperator = " + tm.getSimOperator() + "\n";
            str += "SimOperatorName = " + tm.getSimOperatorName() + "\n";
            str += "SimSerialNumber = " + tm.getSimSerialNumber() + "\n";
            str += "SimState = " + tm.getSimState() + "\n";
            str += "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n";
            str += "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return str;
    }

    /**
     * @param phoneNumber
     */
    public static void dial(final String phoneNumber) {
        ExtUtils.getApp().startActivity(IntentUtils.getDialIntent(phoneNumber));
    }

    /**
     * {@code <uses-permission android:name="android.permission.CALL_PHONE"/>}
     *
     * @param phoneNumber
     */
    public static void call(final String phoneNumber) {
        ExtUtils.getApp().startActivity(IntentUtils.getCallIntent(phoneNumber));
    }

    /**
     * @param phoneNumber
     * @param content
     */
    public static void sendSms(final String phoneNumber, final String content) {
        ExtUtils.getApp().startActivity(IntentUtils.getSendSmsIntent(phoneNumber, content));
    }

    /**
     * {@code <uses-permission android:name="android.permission.SEND_SMS"/>}
     *
     * @param phoneNumber
     * @param content
     * @param lengthMessage = 160
     */
    public static void sendSmsSilent(final String phoneNumber, final String content, final int lengthMessage) {
        try {
            if (StringUtils.isEmpty(content)) return;

            PendingIntent sentIntent = PendingIntent.getBroadcast(ExtUtils.getApp(), 0, new Intent(), 0);
            SmsManager smsManager = SmsManager.getDefault();
            if (content.length() >= lengthMessage) {
                List<String> ms = smsManager.divideMessage(content);
                for (String str : ms) {
                    smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
                }
            } else {
                smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * <p> {@code <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>}</p>
     * <p> {@code <uses-permission android:name="android.permission.READ_CONTACTS"/>}</p>
     *
     * @return
     */
    public static List<HashMap<String, String>> getAllContactInfo() {
        SystemClock.sleep(3000);
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        ContentResolver resolver = ExtUtils.getApp().getContentResolver();
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri date_uri = Uri.parse("content://com.android.contacts/data");

        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, null, null, null);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String contact_id = cursor.getString(0);

                    if (!StringUtils.isEmpty(contact_id)) {
                        Cursor c = resolver.query(date_uri, new String[]{"data1", "mimetype"}, "raw_contact_id=?",
                                new String[]{contact_id}, null);
                        HashMap<String, String> map = new HashMap<>();

                        if (c != null) {
                            while (c.moveToNext()) {
                                String data1 = c.getString(0);
                                String mimetype = c.getString(1);
                                if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                                    map.put("phone", data1);
                                } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                                    map.put("name", data1);
                                }
                            }
                        }

                        list.add(map);

                        if (c != null) {
                            c.close();
                        }
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return list;
    }


    /**
     * <p>{@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>}</p>
     * <p>{@code <uses-permission android:name="android.permission.READ_SMS"/>}</p>
     */
    public static void getAllSMS() {
        ContentResolver resolver = ExtUtils.getApp().getContentResolver();

        Uri uri = Uri.parse("content://sms");
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
        int count = cursor.getCount();

        XmlSerializer xmlSerializer = Xml.newSerializer();
        try {
            xmlSerializer.setOutput(new FileOutputStream(new File("/mnt/sdcard/backupsms.xml")), "utf-8");
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "smss");

            while (cursor.moveToNext()) {
                SystemClock.sleep(1000);
                xmlSerializer.startTag(null, "sms");
                xmlSerializer.startTag(null, "address");
                String address = cursor.getString(0);
                xmlSerializer.text(address);
                xmlSerializer.endTag(null, "address");
                xmlSerializer.startTag(null, "date");
                String date = cursor.getString(1);
                xmlSerializer.text(date);
                xmlSerializer.endTag(null, "date");
                xmlSerializer.startTag(null, "type");
                String type = cursor.getString(2);
                xmlSerializer.text(type);
                xmlSerializer.endTag(null, "type");
                xmlSerializer.startTag(null, "body");
                String body = cursor.getString(3);
                xmlSerializer.text(body);
                xmlSerializer.endTag(null, "body");
                xmlSerializer.endTag(null, "sms");
                System.out.println("address:" + address + "   date:" + date + "  type:" + type + "  body:" + body);
            }
            xmlSerializer.endTag(null, "smss");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
