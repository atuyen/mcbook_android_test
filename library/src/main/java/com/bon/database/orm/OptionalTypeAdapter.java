package com.bon.database.orm;

import android.content.ContentValues;
import android.database.Cursor;

import com.bon.logger.Logger;

/**
 * Created by Dang on 4/4/2016.
 */
public class OptionalTypeAdapter<T> implements TypeAdapter<T> {
    private static final String TAG = OptionalTypeAdapter.class.getSimpleName();

    private final TypeAdapter<T> mWrappedAdapter;

    public OptionalTypeAdapter(TypeAdapter<T> wrappedAdapter) {
        mWrappedAdapter = wrappedAdapter;
    }

    @Override
    public T fromCursor(Cursor c, String columnName) {
        try {
            return c.isNull(c.getColumnIndexOrThrow(columnName))
                    ? null
                    : mWrappedAdapter.fromCursor(c, columnName);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    @Override
    public void toContentValues(ContentValues values, String columnName, T object) {
        try {
            if (object != null) {
                mWrappedAdapter.toContentValues(values, columnName, object);
            } else {
                values.putNull(columnName);
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }
}