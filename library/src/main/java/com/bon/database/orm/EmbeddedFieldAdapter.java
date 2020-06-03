package com.bon.database.orm;

import android.content.ContentValues;
import android.database.Cursor;

import com.bon.logger.Logger;

import java.lang.reflect.Field;

/**
 * Created by Dang on 4/4/2016.
 */
public class EmbeddedFieldAdapter extends FieldAdapter {
    private static final String TAG = EmbeddedFieldAdapter.class.getSimpleName();

    private DaoAdapter<Object> mDaoAdapter;

    @SuppressWarnings("unchecked")
    EmbeddedFieldAdapter(Field field, DaoAdapter<?> daoAdapter) {
        super(field);
        try {
            mDaoAdapter = ((DaoAdapter<Object>) daoAdapter);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void setValueFromCursor(Cursor inCursor, Object outTarget) throws IllegalArgumentException {
        try {
            mField.set(outTarget, mDaoAdapter.fromCursor(inCursor, mDaoAdapter.createInstance()));
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    @Override
    protected void putValueToContentValues(Object value, ContentValues outValues) {
        try {
            mDaoAdapter.toContentValues(outValues, value);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    @Override
    public String[] getColumnNames() {
        try {
            return mDaoAdapter.getProjection();
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    @Override
    public String[] getWritableColumnNames() {
        try {
            return mDaoAdapter.getWritableColumns();
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }
}