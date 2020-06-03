package com.mc.models.gift;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Point implements Serializable {
    @JsonProperty("point")
    private float point;

    public Point() {
    }

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "Point{" +
                "point=" + point +
                '}';
    }
}
