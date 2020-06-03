package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BookProcess implements Serializable {
    @JsonProperty("date")
    private String date;
    @JsonProperty("point")
    private Integer point;

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("point")
    public Integer getPoint() {
        return point;
    }

    @JsonProperty("point")
    public void setPoint(Integer point) {
        this.point = point;
    }
}
