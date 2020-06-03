package com.mc.models.more;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BookLeadBoad implements Serializable {
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("categoryId")
    private Integer categoryId;
    @JsonProperty("price")
    private Object price;
    @JsonProperty("webUri")
    private Object webUri;
    @JsonProperty("linkCommunity")
    private String linkCommunity;
    @JsonProperty("linkShare")
    private String linkShare;
    @JsonProperty("description")
    private String description;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("updatedBy")
    private String updatedBy;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("deletedAt")
    private Object deletedAt;

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
    public Object getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Object price) {
        this.price = price;
    }

    @JsonProperty("webUri")
    public Object getWebUri() {
        return webUri;
    }

    @JsonProperty("webUri")
    public void setWebUri(Object webUri) {
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

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
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
