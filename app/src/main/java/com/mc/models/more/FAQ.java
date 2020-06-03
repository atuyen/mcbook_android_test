package com.mc.models.more;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class FAQ implements Serializable{
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("title")
    private String name;
    @JsonProperty("content")
    private String content;
    @JsonProperty("type")
    private String type;
    @JsonProperty("route")
    private String route;
    @JsonProperty("active")
    private Boolean active;

    public FAQ() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "FAQ{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", route='" + route + '\'' +
                ", active=" + active +
                '}';
    }
}
