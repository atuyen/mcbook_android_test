package com.bon.database.orm;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Dang on 4/4/2016.
 */
public interface TypeAdapter<T> {

    /**
     * Reads a column from cursor and converts it to a Java object. Returns the
     * converted object.
     *
     * @param c          cursor containing the column
     * @param columnName name of the column containing data representing the Java
     *                   object
     * @return the converted Java object. May be null.
     */
    T fromCursor(Cursor c, String columnName);

    /**
     * Converts Java object into type that can be put into {@link ContentValues}
     * and puts it into supplied {@code values} instance under {@code columnName}
     * key.
     *
     * @param values     {@link ContentValues} to be filled with data from
     *                   {@code object}
     * @param columnName name of the target column for converted {@code object}
     * @param object     an object to be converted
     */
    void toContentValues(ContentValues values, String columnName, T object);
}