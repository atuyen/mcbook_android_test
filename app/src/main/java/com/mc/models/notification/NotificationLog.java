package com.mc.models.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class NotificationLog implements Serializable {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("lastTime")
    private String lastTime;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("deletedAt")
    private Object deletedAt;


    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("lastTime")
    public String getLastTime() {
        return lastTime;
    }

    @JsonProperty("lastTime")
    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("updatedAt")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updatedAt")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("deletedAt")
    public Object getDeletedAt() {
        return deletedAt;
    }

    @JsonProperty("deletedAt")
    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }
}
