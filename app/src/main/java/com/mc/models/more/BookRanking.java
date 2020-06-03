package com.mc.models.more;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookRanking {

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
    @JsonProperty("totalLesson")
    private Integer totalLesson;
    @JsonProperty("totalAudio")
    private Integer totalAudio;
    @JsonProperty("totalVideo")
    private Integer totalVideo;
    @JsonProperty("totalText")
    private Integer totalText;
    @JsonProperty("point")
    private String point;

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

    @JsonProperty("totalLesson")
    public Integer getTotalLesson() {
        return totalLesson;
    }

    @JsonProperty("totalLesson")
    public void setTotalLesson(Integer totalLesson) {
        this.totalLesson = totalLesson;
    }

    @JsonProperty("totalAudio")
    public Integer getTotalAudio() {
        return totalAudio;
    }

    @JsonProperty("totalAudio")
    public void setTotalAudio(Integer totalAudio) {
        this.totalAudio = totalAudio;
    }

    @JsonProperty("totalVideo")
    public Integer getTotalVideo() {
        return totalVideo;
    }

    @JsonProperty("totalVideo")
    public void setTotalVideo(Integer totalVideo) {
        this.totalVideo = totalVideo;
    }

    @JsonProperty("totalText")
    public Integer getTotalText() {
        return totalText;
    }

    @JsonProperty("totalText")
    public void setTotalText(Integer totalText) {
        this.totalText = totalText;
    }

    @JsonProperty("point")
    public String getPoint() {
        return point;
    }

    @JsonProperty("point")
    public void setPoint(String point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "BookRanking{" +
                "avatar='" + avatar + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", webUri='" + webUri + '\'' +
                ", linkCommunity='" + linkCommunity + '\'' +
                ", linkShare='" + linkShare + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", deletedAt=" + deletedAt +
                ", totalLesson=" + totalLesson +
                ", totalAudio=" + totalAudio +
                ", totalVideo=" + totalVideo +
                ", totalText=" + totalText +
                ", point='" + point + '\'' +
                '}';
    }
}
