package com.mc.models.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Condition {
    @JsonProperty("ids")
    private List<Integer> ids = null;
    @JsonProperty("type")
    private String type;

    @JsonProperty("ids")
    public List<Integer> getIds() {
        return ids;
    }

    @JsonProperty("ids")
    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }
}
