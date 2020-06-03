package com.bon.database.orm;

import android.content.ContentValues;
import android.database.Cursor;

import com.bon.logger.Logger;

/**
 * Created by Dang on 4/4/2016.
 */
public final class TypeAdapters {
    private TypeAdapters() {
    }

    public static class StringAdapter implements TypeAdapter<String> {
        private static final String TAG = StringAdapter.class.getSimpleName();

        @Override
        public String fromCursor(Cursor c, String columnName) {
            try {
                return c.getString(c.getColumnIndexOrThrow(columnName));
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }

            return null;
        }

        @Override
        public void toContentValues(ContentValues values, String columnName, String object) {
            try {
                values.put(columnName, object);
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }
        }
    }

    public static class ShortAdapter implements TypeAdapter<Short> {
        private static final String TAG = ShortAdapter.class.getSimpleName();

        @Override
        public Short fromCursor(Cursor c, String columnName) {
            try {
                return c.getShort(c.getColumnIndexOrThrow(columnName));
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }

            return null;
        }

        @Override
        public void toContentValues(ContentValues values, String columnName, Short object) {
            try {
                values.put(columnName, object);
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }
        }
    }

    public static class IntegerAdapter implements TypeAdapter<Integer> {
        private static final String TAG = IntegerAdapter.class.getSimpleName();

        @Override
        public Integer fromCursor(Cursor c, String columnName) {
            try {
                return c.getInt(c.getColumnIndexOrThrow(columnName));
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }

            return null;
        }

        @Override
        public void toContentValues(ContentValues values, String columnName, Integer object) {
            try {
                values.put(columnName, object);
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }
        }
    }

    public static class LongAdapter implements TypeAdapter<Long> {
        private static final String TAG = LongAdapter.class.getSimpleName();

        @Override
        public Long fromCursor(Cursor c, String columnName) {
            try {
                return c.getLong(c.getColumnIndexOrThrow(columnName));
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }

            return null;
        }

        @Override
        public void toContentValues(ContentValues values, String columnName, Long object) {
            try {
                values.put(columnName, object);
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }
        }
    }

    public static class FloatAdapter implements TypeAdapter<Float> {
        private static final String TAG = FloatAdapter.class.getSimpleName();

        @Override
        public Float fromCursor(Cursor c, String columnName) {
            try {
                return c.getFloat(c.getColumnIndexOrThrow(columnName));
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }

            return null;
        }

        @Override
        public void toContentValues(ContentValues values, String columnName, Float object) {
            try {
                values.put(columnName, object);
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }
        }
    }

    public static class DoubleAdapter implements TypeAdapter<Double> {
        private static final String TAG = DoubleAdapter.class.getSimpleName();

        @Override
        public Double fromCursor(Cursor c, String columnName) {
            try {
                return c.getDouble(c.getColumnIndexOrThrow(columnName));
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }

            return null;
        }

        @Override
        public void toContentValues(ContentValues values, String columnName, Double object) {
            try {
                values.put(columnName, object);
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }
        }
    }

    public static class BooleanAdapter implements TypeAdapter<Boolean> {
        private static final String TAG = BooleanAdapter.class.getSimpleName();

        @Override
        public Boolean fromCursor(Cursor c, String columnName) {
            try {
                return c.getInt(c.getColumnIndexOrThrow(columnName)) > 0;
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }

            return false;
        }

        @Override
        public void toContentValues(ContentValues values, String columnName, Boolean object) {
            try {
                values.put(columnName, object);
            } catch (Exception ex) {
                Logger.e(TAG, ex);
            }
        }
    }
}