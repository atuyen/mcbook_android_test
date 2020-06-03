package com.mc.models.gift;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Gift implements Serializable {
    @JsonProperty("isNew")
    private Boolean isNew;
    @JsonProperty("coverUri")
    private String coverUri;
    @JsonProperty("contentUri")
    private String contentUri;
    @JsonProperty("subsUri")
    private String subsUri;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;
    @JsonProperty("bookAuthor")
    private String bookAuthor;
    @JsonProperty("examId")
    private Integer examId;
    @JsonProperty("examNumber")
    private Integer examNumber;

    @JsonIgnoreProperties
    @JsonProperty("examData")
    private ExamData examData;

    public Gift() {

    }

    public ExamData getExamData() {
        return examData;
    }

    public void setExamData(ExamData examData) {
        this.examData = examData;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getSubsUri() {
        return subsUri;
    }

    public void setSubsUri(String subsUri) {
        this.subsUri = subsUri;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public Integer getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(Integer examNumber) {
        this.examNumber = examNumber;
    }

    @Override
    public String toString() {
        return "Gift{" +
                "isNew=" + isNew +
                ", coverUri='" + coverUri + '\'' +
                ", contentUri='" + contentUri + '\'' +
                ", subsUri='" + subsUri + '\'' +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", examId=" + examId +
                ", examNumber=" + examNumber +
                ", examData=" + examData +
                '}';
    }
}
