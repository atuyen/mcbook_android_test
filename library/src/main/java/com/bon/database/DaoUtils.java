package com.bon.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bon.logger.Logger;

import java.util.List;

@SuppressWarnings("ALL")
public class DaoUtils {
    private static final String TAG = DaoUtils.class.getSimpleName();
    private static SQLiteDatabase sqLiteDatabase = null;

    /**
     * new instance of SQLiteOpenHelper, often used in application
     *
     * @param sqLiteOpenHelper
     */
    public static void newInstance(SQLiteOpenHelper sqLiteOpenHelper) {
        if (sqLiteDatabase == null) {
            synchronized (DaoUtils.class) {
                if (sqLiteDatabase == null) {
                    sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
                }
            }
        }
    }

    /**
     * insert a record into database
     *
     * @param tableName
     * @param values
     * @return
     */
    public static long insert(String tableName, ContentValues values) {
        long id = 0;

        try {
            sqLiteDatabase.beginTransaction();
            id = sqLiteDatabase.insert(tableName, null, values);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, e);
        } finally {
            sqLiteDatabase.endTransaction();
        }

        return id;
    }

    /**
     * insert multiple records into database
     *
     * @param tableName
     * @param values
     * @return
     */
    public static boolean inserts(String tableName,
                                  List<ContentValues> values) {
        try {
            if (values == null || values.size() <= 0) return false;

            sqLiteDatabase.beginTransaction();
            for (ContentValues value : values) {
                sqLiteDatabase.insert(tableName, null, value);
            }
            sqLiteDatabase.setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            Logger.e(TAG, e);
        } finally {
            sqLiteDatabase.endTransaction();
        }

        return false;
    }

    /**
     * update record
     *
     * @param tableName
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public static long update(String tableName, ContentValues values,
                              String whereClause, String[] whereArgs) {
        long id = 0;

        try {
            sqLiteDatabase.beginTransaction();
            id = sqLiteDatabase.update(tableName, values, whereClause, whereArgs);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, e);
        } finally {
            sqLiteDatabase.endTransaction();
        }

        return id;
    }

    /**
     * delete records
     *
     * @param tableName
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public static long delete(String tableName, String whereClause,
                              String[] whereArgs) {
        long id = 0;

        try {
            sqLiteDatabase.beginTransaction();
            id = sqLiteDatabase.delete(tableName, whereClause, whereArgs);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, e);
        } finally {
            sqLiteDatabase.endTransaction();
        }

        return id;
    }

    /**
     * query data from database
     *
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return
     */
    public static Cursor query(String table, String[] columns,
                               String selection, String[] selectionArgs, String groupBy,
                               String having, String orderBy) {
        Cursor cursor = null;

        try {
            cursor = sqLiteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return cursor;
    }

    /**
     * exec query string
     *
     * @param sql
     */
    public static void execSQL(String sql) {
        execSQL(sql, null);
    }

    /**
     * exec query string
     *
     * @param sql
     * @param bindArgs
     */
    public static void execSQL(String sql, String[] bindArgs) {
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.execSQL(sql, bindArgs);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, e);
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    /**
     * get data by query string
     *
     * @param sql
     * @return
     */
    public static Cursor rawQuery(String sql) {
        return rawQuery(sql, null);
    }

    /**
     * get data by query string
     *
     * @param sql
     * @param selectionArgs
     * @return
     */
    public static Cursor rawQuery(String sql, String[] selectionArgs) {
        Cursor cursor = null;

        try {
            cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return cursor;
    }
}
