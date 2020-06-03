package com.bon.database.orm;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Dang on 4/4/2016.
 */
public interface DaoAdapter<T> {
    T createInstance();

    T fromCursor(Cursor c, T object);

    ContentValues toContentValues(ContentValues values, T object);

    ContentValues createContentValues();

    String[] getProjection();

    String[] getWritableColumns();
}