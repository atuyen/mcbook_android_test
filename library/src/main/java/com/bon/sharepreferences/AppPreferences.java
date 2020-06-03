package com.bon.sharepreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bon.encrypted.AESCrypt;
import com.bon.jackson.JacksonUtils;
import com.bon.logger.Logger;
import com.bon.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;

public class AppPreferences {
    private static final String TAG = AppPreferences.class.getSimpleName();

    // key password
    private static final String KEY_PASSWORD_ENCRYPT = "123456789poiuytrewq";

    // instance
    private static AppPreferences instance = null;
    private static AESCrypt aesCrypt = null;

    // variable
    private SharedPreferences appSharedPrefs = null;
    private Editor prefsEditor = null;

    /**
     * getInstance
     *
     * @param context
     */
    public static AppPreferences getInstance(Context context) {
        try {
            if (instance == null) {
                synchronized (AppPreferences.class) {
                    // instance of app
                    if (instance == null) {
                        instance = new AppPreferences(context);
                    }

                    // instance AES crypt
                    if (aesCrypt == null) {
                        aesCrypt = new AESCrypt(KEY_PASSWORD_ENCRYPT);
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return instance;
    }

    /**
     * @param context
     */
    @SuppressLint("CommitPrefEdits")
    public AppPreferences(Context context) {
        try {
            appSharedPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            prefsEditor = appSharedPrefs.edit();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * put value int
     *
     * @param key
     * @param value
     */
    public AppPreferences putInt(String key, int value) {
        try {
            prefsEditor.putInt(key, value);
            prefsEditor.commit();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * get value int
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        try {
            return appSharedPrefs.getInt(key, 0);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return 0;
    }

    public AppPreferences putJson(String key, Object value) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(value);
            prefsEditor.putString(key, json);
            prefsEditor.commit();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * get value int
     *
     * @param key
     * @return
     */
    public <T> T getJson(String key, Class<T> clazz ) {
        try {
            Gson gson = new Gson();
            String json =  appSharedPrefs.getString(key, "");
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
        return null;
    }

    /**
     * put value long
     *
     * @param key
     * @param value
     */
    public AppPreferences putLong(String key, long value) {
        try {
            prefsEditor.putLong(key, value);
            prefsEditor.commit();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * get value long
     *
     * @param key
     * @return
     */
    public long getLong(String key) {
        try {
            return appSharedPrefs.getLong(key, 0);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return 0;
    }

    /**
     * put value float
     *
     * @param key
     * @param value
     */
    public AppPreferences putFloat(String key, float value) {
        try {
            prefsEditor.putFloat(key, value);
            prefsEditor.commit();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * get value long
     *
     * @param key
     * @return
     */
    public float getFloat(String key) {
        try {
            return appSharedPrefs.getFloat(key, 0f);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return 0f;
    }

    /**
     * put value boolean
     *
     * @param key
     * @param value
     */
    public AppPreferences putBoolean(String key, boolean value) {
        try {
            prefsEditor.putBoolean(key, value);
            prefsEditor.commit();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * get value boolean
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        try {
            return appSharedPrefs.getBoolean(key, false);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return false;
    }

    /**
     * put value String
     *
     * @param key
     * @param value
     */
    public AppPreferences putString(String key, String value) {
        try {
            prefsEditor.putString(key, value);
            prefsEditor.commit();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * put value String
     *
     * @param key
     * @param value
     */
    public AppPreferences putStringEncrypt(String key, String value) {
        try {
            String result = value;
            if (!StringUtils.isEmpty(result)) {
                result = aesCrypt.encrypt(result);
            }

            prefsEditor.putString(key, result);
            prefsEditor.commit();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }


    /**
     * get value String
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        try {
            return appSharedPrefs.getString(key, "");
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }

    /**
     * get value String
     *
     * @param key
     * @return
     */
    public String getStringDecrypt(String key) {
        try {
            String result = appSharedPrefs.getString(key, "");
            if (!StringUtils.isEmpty(result)) {
                result = aesCrypt.decrypt(result);
            }

            return result;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }

    /**
     * put object
     *
     * @param key
     * @param object
     * @param <T>
     * @return
     */
    public <T> AppPreferences putObject(String key, T object) {
        try {
            prefsEditor.putString(key, JacksonUtils.writeValueToString(object));
            prefsEditor.commit();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * @param key
     * @param object
     * @param <T>
     * @return
     */
    public <T> AppPreferences putObjectEncrypt(String key, T object) {
        try {
            String result = JacksonUtils.writeValueToString(object);
            if (!StringUtils.isEmpty(result)) {
                result = aesCrypt.encrypt(result);
            }

            prefsEditor.putString(key, result);
            prefsEditor.commit();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * get object
     *
     * @param key
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> T getObject(String key, TypeReference<T> typeReference) {
        try {
            String value = appSharedPrefs.getString(key, null);
            if (StringUtils.isEmpty(value)) return null;

            return JacksonUtils.convertJsonToObject(value, typeReference);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    public <T> T getObjectDecrypt(String key, TypeReference<T> typeReference) {
        try {
            String result = appSharedPrefs.getString(key, null);
            if (StringUtils.isEmpty(result)) return null;

            result = aesCrypt.decrypt(result);
            return JacksonUtils.convertJsonToObject(result, typeReference);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getObject(String key, Class<T> clazz) {
        try {
            String value = appSharedPrefs.getString(key, null);
            if (StringUtils.isEmpty(value)) return null;

            return JacksonUtils.convertJsonToObject(value, clazz);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    public <T> T getObjectDecrypt(String key, Class<T> clazz) {
        try {
            String result = appSharedPrefs.getString(key, null);
            if (StringUtils.isEmpty(result)) return null;

            result = aesCrypt.decrypt(result);
            return JacksonUtils.convertJsonToObject(result, clazz);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }


    /**
     * clear all cache in SharedPreference
     */
    public AppPreferences clearCache() {
        try {
            prefsEditor.clear().commit();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }
}
