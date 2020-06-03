package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class AddBook implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("bookId")
    private Integer bookId;
    @JsonProperty("giftId")
    private Object giftId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("code")
    private String code;
    @JsonProperty("process")
    private Integer process;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("bookName")
    private String bookName;

    public AddBook() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Object getGiftId() {
        return giftId;
    }

    public void setGiftId(Object giftId) {
        this.giftId = giftId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getProcess() {
        return process;
    }

    public void setProcess(Integer process) {
        this.process = process;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "AddBook{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", bookId=" + bookId +
                ", giftId=" + giftId +
                ", type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", process=" + process +
                ", active=" + active +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}
