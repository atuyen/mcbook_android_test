package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class LogLesson implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("lessonId")
    private Integer lessonId;
    @JsonProperty("userId")
    private String userId;

    public LogLesson() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LogLesson{" +
                "id=" + id +
                ", lessonId=" + lessonId +
                ", userId='" + userId + '\'' +
                '}';
    }
}
