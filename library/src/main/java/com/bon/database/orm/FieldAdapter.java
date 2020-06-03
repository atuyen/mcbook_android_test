package com.bon.database.orm;

import android.content.ContentValues;
import android.database.Cursor;

import com.bon.logger.Logger;

import java.lang.reflect.Field;

/**
 * Created by Dang on 4/4/2016.
 */
public abstract class FieldAdapter {
    private static final String TAG = FieldAdapter.class.getSimpleName();
    protected final Field mField;

    FieldAdapter(Field field) {
        mField = field;
    }

    public abstract void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException;

    public void putToContentValues(Object inObject, ContentValues outValues) {
        try {
            Object value = inObject != null ? mField.get(inObject) : null;
            putValueToContentValues(value, outValues);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    protected abstract void putValueToContentValues(Object value, ContentValues outValues);

    public abstract String[] getColumnNames();

    public abstract String[] getWritableColumnNames();
}