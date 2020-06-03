package com.bon.util;

import android.annotation.TargetApi;
import android.os.Build;

import com.bon.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 09/02/2017.
 */

public class ReflectionUtils {
    private static final String TAG = ReflectionUtils.class.getSimpleName();

    public static Field getField(Class clazz, String fieldName) {
        try {
            final Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    public static Object getValue(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    public static void setValue(Field field, Object obj, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            Logger.e(TAG, e);
        }
    }

    public static Method getMethod(Class clazz, String methodName) {
        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                return method;
            }
        }

        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void invokeMethod(Object object, Method method, Object... args) {
        try {
            if (method == null) return;
            method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Logger.e(TAG, e);
        }
    }
}
