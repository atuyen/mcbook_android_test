package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class BookResponse implements Serializable {
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("categoryId")
    private Integer categoryId;
    @JsonProperty("linkCommunity")
    private String linkCommunity;
    @JsonProperty("linkShare")
    private String linkShare;
    @JsonProperty("description")
    private String description;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("authors")
    private List<Author> authors;
    @JsonProperty("process")
    private Integer process;
    @JsonProperty("isNew")
    private boolean isNew;
    @JsonProperty("chapters")
    private List<Chapter> chapters = null;
    @JsonProperty("totalChapter")
    private Integer totalChapter;
    @JsonProperty("totalLesson")
    private Integer totalLesson;
    @JsonProperty("related")
    private List<RelatedBook> realtedBooks = null;

    public BookResponse() {
    }

    public List<RelatedBook> getRealtedBooks() {
        return realtedBooks;
    }

    public void setRealtedBooks(List<RelatedBook> realtedBooks) {
        this.realtedBooks = realtedBooks;
    }

    public Integer getProcess() {
        return process;
    }

    public void setProcess(Integer process) {
        this.process = process;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }


    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getLinkCommunity() {
        return linkCommunity;
    }

    public void setLinkCommunity(String linkCommunity) {
        this.linkCommunity = linkCommunity;
    }

    public String getLinkShare() {
        return linkShare;
    }

    public void setLinkShare(String linkShare) {
        this.linkShare = linkShare;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public Integer getTotalChapter() {
        return totalChapter;
    }

    public void setTotalChapter(Integer totalChapter) {
        this.totalChapter = totalChapter;
    }

    public Integer getTotalLesson() {
        return totalLesson;
    }

    public void setTotalLesson(Integer totalLesson) {
        this.totalLesson = totalLesson;
    }

    @Override
    public String toString() {
        return "BookResponse{" +
                "avatar='" + avatar + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", linkCommunity='" + linkCommunity + '\'' +
                ", linkShare='" + linkShare + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                ", authors=" + authors +
                ", process=" + process +
                ", isNew=" + isNew +
                ", chapters=" + chapters +
                ", totalChapter=" + totalChapter +
                ", totalLesson=" + totalLesson +
                ", realtedBooks=" + realtedBooks +
                '}';
    }
}
