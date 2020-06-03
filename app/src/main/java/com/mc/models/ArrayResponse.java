package com.mc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class ArrayResponse<T> implements Serializable {
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("rows")
    private List<T> rows = null;
    @JsonProperty("point")
    private String point;

    public ArrayResponse() {
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "ArrayResponse{" +
                "count=" + count +
                ", rows=" + rows +
                ", point='" + point + '\'' +
                '}';
    }
}
