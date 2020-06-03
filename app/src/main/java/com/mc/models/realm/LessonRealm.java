package com.mc.models.realm;


import com.mc.utilities.Constant;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LessonRealm extends RealmObject implements Serializable {

    public static final String ID = "id";
    public static final String PROFRESS = "progress";
    public static final String BOOK_ID = "bookId";
    public static final String DOWNLOAD_STATUS = "downloadStatus";
    public static final String DOWNLOAD_ID = "downloadId";
    public static final String NAME = "name";
    public static final String FILE_NAME = "fileName";
    public static final String INDEX = "index";
    public static final String DOWNLOAD_TYPE = "downloadType";
    public static final String GIFT_ID = "giftId";
    public static final String USER_ID = "userId";

    @PrimaryKey
    private String id;
    private int progress;
    private int bookId;
    private int giftId;
    private int downloadStatus = Constant.NONE_DOWNLOAD;
    private int downloadId;
    private String name;
    private String fileName;
    private int index;
    private int downloadType;
    private String userId;

    public LessonRealm() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(int downloadType) {
        this.downloadType = downloadType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    @Override
    public String toString() {
        return "LessonRealm{" +
                "id=" + id +
                ", progress=" + progress +
                ", bookId=" + bookId +
                ", giftId=" + giftId +
                ", downloadStatus=" + downloadStatus +
                ", downloadId=" + downloadId +
                ", name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", index=" + index +
                ", downloadType=" + downloadType +
                '}';
    }
}
