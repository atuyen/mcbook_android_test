package com.bon.database;

import android.database.Cursor;

import com.bon.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Information of database
 * Created by Dang on 10/28/2015.
 */
@SuppressWarnings("ALL")
public class DatabaseInfo {
    private static final String TAG = DatabaseInfo.class.getSimpleName();

    private String type = null;
    private String name = null;
    private String tblName = null;
    private int rootPage = 0;
    private String sql = null;

    public DatabaseInfo() {
        this(null, null, null, 0, null);
    }

    public DatabaseInfo(String type, String name, String tblName, int rootPage, String sql) {
        this.type = type;
        this.name = name;
        this.tblName = tblName;
        this.rootPage = rootPage;
        this.sql = sql;
    }

    /**
     * get info database
     *
     * @param cursor
     * @return
     */
    public static List<DatabaseInfo> getTableInfoEntities(Cursor cursor) {
        List<DatabaseInfo> databaseInfoEntities = null;

        try {
            if (cursor != null && cursor.moveToFirst()) {
                databaseInfoEntities = new ArrayList<>();
                do {
                    DatabaseInfo databaseInfoEntity = new DatabaseInfo();
                    databaseInfoEntity.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
                    databaseInfoEntity.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    databaseInfoEntity.setTblName(cursor.getString(cursor.getColumnIndexOrThrow("tbl_name")));
                    databaseInfoEntity.setRootPage(cursor.getInt(cursor.getColumnIndexOrThrow("rootpage")));
                    databaseInfoEntity.setSql(cursor.getString(cursor.getColumnIndexOrThrow("sql")));
                    databaseInfoEntities.add(databaseInfoEntity);
                } while (cursor.moveToNext());
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return databaseInfoEntities;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTblName() {
        return tblName;
    }

    public void setTblName(String tblName) {
        this.tblName = tblName;
    }

    public int getRootPage() {
        return rootPage;
    }

    public void setRootPage(int rootPage) {
        this.rootPage = rootPage;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
