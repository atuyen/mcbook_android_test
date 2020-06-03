package com.bon.database;

import android.database.Cursor;

import com.bon.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Information of table in sqlite
 * Created by Dang on 10/28/2015.
 */
@SuppressWarnings("ALL")
public class TableInfo {
    private static final String TAG = TableInfo.class.getSimpleName();

    private int cid = 0;
    private String name = null;
    private String type = null;
    private int notNull = 0;
    private int pk = 0;

    /**
     * constructor
     */
    public TableInfo() {
        this(0, null, null, 0, 0);
    }

    /**
     * @param cid
     * @param name
     * @param type
     * @param notNull
     * @param pk
     */
    public TableInfo(int cid, String name, String type, int notNull, int pk) {
        this.cid = cid;
        this.name = name;
        this.type = type;
        this.notNull = notNull;
        this.pk = pk;
    }

    /**
     * get object from cursor
     *
     * @param cursor
     * @return
     */
    public static List<TableInfo> getTableInfoEntities(Cursor cursor) {
        List<TableInfo> tableInfoEntities = null;

        try {
            if (cursor != null && cursor.moveToFirst()) {
                tableInfoEntities = new ArrayList<>();
                do {
                    TableInfo tableInfoEntity = new TableInfo();
                    tableInfoEntity.setCid(cursor.getInt(cursor.getColumnIndexOrThrow("cid")));
                    tableInfoEntity.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    tableInfoEntity.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
                    tableInfoEntity.setNotNull(cursor.getInt(cursor.getColumnIndexOrThrow("notnull")));
                    tableInfoEntity.setPk(cursor.getInt(cursor.getColumnIndexOrThrow("pk")));
                    tableInfoEntities.add(tableInfoEntity);
                } while (cursor.moveToNext());
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return tableInfoEntities;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNotNull() {
        return notNull;
    }

    public void setNotNull(int notNull) {
        this.notNull = notNull;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "cid=" + cid +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", notNull=" + notNull +
                ", pk=" + pk +
                '}';
    }
}
