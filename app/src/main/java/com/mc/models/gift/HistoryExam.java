package com.mc.models.gift;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class HistoryExam implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("startAt")
    private String startAt;
    @JsonProperty("point")
    private float point;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("startAt")
    public String getStartAt() {
        return startAt;
    }

    @JsonProperty("startAt")
    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    @JsonProperty("point")
    public float getPoint() {
        return point;
    }

    @JsonProperty("point")
    public void setPoint(float point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "HistoryExam{" +
                "id=" + id +
                ", startAt='" + startAt + '\'' +
                ", point=" + point +
                '}';
    }
}
