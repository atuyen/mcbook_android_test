package com.mc.models.home;


import java.io.Serializable;

/**
 * Created by tuant on 19-Nov-18.
 */

public class RecordItem implements Serializable {
    private int id;
    private String name;
    private String path;
    private long length;
    private long time;
    private String role;

    public RecordItem() {
    }

    public RecordItem(String name, String path, long length, long time, String role) {
        this.name = name;
        this.length = length;
        this.time = time;
        this.path = path;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String mPath) {
        this.path = mPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        this.name = mName;
    }

    public int getId() {
        return id;
    }

    public void setId(int mId) {
        this.id = mId;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long mLength) {
        this.length = mLength;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long mTime) {
        this.time = mTime;
    }

    @Override
    public String toString() {
        return "RecordItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", length=" + length +
                ", time=" + time +
                '}';
    }
}
