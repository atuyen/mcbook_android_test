package com.mc.models.more;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class LeadBroad implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("avatar")
    private String picture;
    @JsonProperty("point")
    private Integer point;

    public LeadBroad() {
    }

    public LeadBroad(Integer id, String name, String picture, Integer point) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.point = point;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "LeadBroad{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", point='" + point + '\'' +
                '}';
    }
}
