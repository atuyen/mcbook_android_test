package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Lesson implements Serializable {

    @PrimaryKey
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("media")
    private String media;
    @JsonProperty("thumbnail")
    private String thumbnail;
    @JsonProperty("name")
    private String name;
    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("type")
    private String type;
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("subtitle")
    private String subtitle;
    @JsonProperty("order")
    private Integer order;
    @JsonProperty("code")
    private String code;
    @JsonProperty("checked")
    private Boolean checked;

    int index;

    String fileName;
    String subFile;
    int progress;
    int bookId;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Lesson() {
    }

    public Lesson(String type) {
    this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSubFile() {
        return subFile;
    }

    public void setSubFile(String subFile) {
        this.subFile = subFile;
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

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", media='" + media + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", type='" + type + '\'' +
                ", page=" + page +
                ", subtitle='" + subtitle + '\'' +
                ", order=" + order +
                ", code='" + code + '\'' +
                ", checked=" + checked +
                ", fileName='" + fileName + '\'' +
                ", subFile='" + subFile + '\'' +
                ", progress=" + progress +
                ", bookId=" + bookId +
                '}';
    }
}
