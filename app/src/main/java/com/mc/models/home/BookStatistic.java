package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BookStatistic implements Serializable {
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("categoryId")
    private Integer categoryId;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("webUri")
    private String webUri;
    @JsonProperty("linkCommunity")
    private String linkCommunity;
    @JsonProperty("linkShare")
    private String linkShare;
    @JsonProperty("description")
    private String description;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("deletedAt")
    private Object deletedAt;
    @JsonProperty("process")
    private Integer process;
    @JsonProperty("totalPoint")
    private Integer totalPoint;
    @JsonProperty("studyTime")
    private Integer studyTime;

    @JsonProperty("avatar")
    public String getAvatar() {
        return avatar;
    }

    @JsonProperty("avatar")
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("categoryId")
    public Integer getCategoryId() {
        return categoryId;
    }

    @JsonProperty("categoryId")
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @JsonProperty("price")
    public Integer getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Integer price) {
        this.price = price;
    }

    @JsonProperty("webUri")
    public String getWebUri() {
        return webUri;
    }

    @JsonProperty("webUri")
    public void setWebUri(String webUri) {
        this.webUri = webUri;
    }

    @JsonProperty("linkCommunity")
    public String getLinkCommunity() {
        return linkCommunity;
    }

    @JsonProperty("linkCommunity")
    public void setLinkCommunity(String linkCommunity) {
        this.linkCommunity = linkCommunity;
    }

    @JsonProperty("linkShare")
    public String getLinkShare() {
        return linkShare;
    }

    @JsonProperty("linkShare")
    public void setLinkShare(String linkShare) {
        this.linkShare = linkShare;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(Boolean active) {
        this.active = active;
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

    @JsonProperty("process")
    public Integer getProcess() {
        return process;
    }

    @JsonProperty("process")
    public void setProcess(Integer process) {
        this.process = process;
    }

    @JsonProperty("totalPoint")
    public Integer getTotalPoint() {
        return totalPoint;
    }

    @JsonProperty("totalPoint")
    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    @JsonProperty("studyTime")
    public Integer getStudyTime() {
        return studyTime;
    }

    @JsonProperty("studyTime")
    public void setStudyTime(Integer studyTime) {
        this.studyTime = studyTime;
    }
}
