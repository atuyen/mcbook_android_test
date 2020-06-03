package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Point implements Serializable {
    @JsonProperty("current")
    private Integer current;
    @JsonProperty("total")
    private Integer total;

    public Point() {
    }

    public Point(Integer current, Integer total) {
        this.current = current;
        this.total = total;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Point{" +
                "current=" + current +
                ", total=" + total +
                '}';
    }
}
