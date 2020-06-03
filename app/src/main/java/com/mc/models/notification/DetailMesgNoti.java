package com.mc.models.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DetailMesgNoti {
    @JsonProperty("mediaUri")
    private String mediaUri;
    @JsonProperty("title")
    private String title;
    @JsonProperty("content")
    private String content;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("name")
    private String name;

    @JsonProperty("mediaUri")
    public String getMediaUri() {
        return mediaUri;
    }

    @JsonProperty("mediaUri")
    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("avatar")
    public String getAvatar() {
        return avatar;
    }

    @JsonProperty("avatar")
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DetailMesgNoti{" +
                "mediaUri='" + mediaUri + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
