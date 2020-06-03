package com.mc.models.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class UnReadNotification implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("action")
    private String action;
    @JsonProperty("dataId")
    private Integer dataId;
    @JsonProperty("isRead")
    private Boolean isRead;
    @JsonProperty("sendId")
    private String sendId;
    @JsonProperty("receiveId")
    private String receiveId;
    @JsonProperty("createdBy")
    private Object createdBy;
    @JsonProperty("updatedBy")
    private String updatedBy;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;
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

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("action")
    public String getAction() {
        return action;
    }

    @JsonProperty("action")
    public void setAction(String action) {
        this.action = action;
    }

    @JsonProperty("dataId")
    public Integer getDataId() {
        return dataId;
    }

    @JsonProperty("dataId")
    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    @JsonProperty("isRead")
    public Boolean getIsRead() {
        return isRead;
    }

    @JsonProperty("isRead")
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    @JsonProperty("sendId")
    public String getSendId() {
        return sendId;
    }

    @JsonProperty("sendId")
    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    @JsonProperty("receiveId")
    public String getReceiveId() {
        return receiveId;
    }

    @JsonProperty("receiveId")
    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    @JsonProperty("createdBy")
    public Object getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("updatedBy")
    public String getUpdatedBy() {
        return updatedBy;
    }

    @JsonProperty("updatedBy")
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("updatedAt")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updatedAt")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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
