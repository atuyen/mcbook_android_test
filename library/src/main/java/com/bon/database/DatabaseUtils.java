package com.bon.database;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.bon.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import java8.util.stream.StreamSupport;

/**
 * Created by Dang on 10/14/2015.
 */
public class DatabaseUtils {
    private static final String TAG = DatabaseUtils.class.getSimpleName();

    /**
     * path of database on sdcard
     */
    private static final String databasePath = "";

    /**
     * SQLite database
     */
    private static SQLiteDatabase sqLiteDatabase;

    /**
     * copy database to folder backup
     *
     * @param packageName
     * @param databaseName
     * @param folderBackup
     */
    public static void copyDatabase(String packageName, String databaseName, String folderBackup) {
        try {
            boolean success;
            File file = new File(Environment.getExternalStorageDirectory() + "/" + folderBackup);
            // create folder to backup database
            success = file.exists() || file.mkdir();

            if (success) {
                // path database
                @SuppressLint("SdCardPath")
                String inFileName = "/data/data/" + packageName + "/databases/" + databaseName;
                File dbFile = new File(inFileName);
                FileInputStream fis = new FileInputStream(dbFile);

                // path database backup
                File outFileName = new File(file.getAbsolutePath(), databaseName);
                if (outFileName.exists()) outFileName.delete();

                //Open the empty db as the output stream
                OutputStream output = new FileOutputStream(outFileName);

                //transfer bytes from the input file to the output file
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                output.flush();
                output.close();
                fis.close();
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    /**
     * check database is exist follow by database path
     *
     * @param databasePath
     * @return
     */
    public static boolean checkDatabase(String databasePath) {
        boolean isExist = false;

        try {
            File databaseFile = new File(databasePath);
            isExist = databaseFile.exists();
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return isExist;
    }

    /**
     * check column existed in table
     *
     * @param db
     * @param tableName
     * @param columnName
     * @return true if existed else return false
     */
    public static boolean existColumnInTable(SQLiteDatabase db, String tableName, String columnName) {
        try {
            // get info table
            Cursor cursor = db.rawQuery("PRAGMA table_info(?)", new String[]{tableName});

            // convertObjectToMapString cursor to object
            List<TableInfo> tableInfoEntities = TableInfo.getTableInfoEntities(cursor);
            if (tableInfoEntities != null && tableInfoEntities.size() > 0) {
                return StreamSupport.stream(tableInfoEntities).filter(n -> n.getName().equalsIgnoreCase(columnName)).count() > 0;
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return false;
    }

    /**
     * get info of database
     *
     * @param db
     * @return
     */
    public static List<DatabaseInfo> getDatabaseInfo(SQLiteDatabase db) {
        try {
            return DatabaseInfo.getTableInfoEntities(db.rawQuery("select * from sqlite_master", null));
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return null;
    }

    /**
     * open database
     */
    public static void openDatabase() {
        //Open the database
        try {
            sqLiteDatabase = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    /**
     * close database
     */
    public static void close() {
        try {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }
}
