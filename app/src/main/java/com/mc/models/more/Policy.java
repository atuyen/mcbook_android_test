package com.mc.models.more;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Policy implements Serializable{
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("content")
    private List<String> content;

    public Policy() {
    }

    public Policy(Integer id, String name, List<String> content) {
        this.id = id;
        this.name = name;
        this.content = content;
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

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Policy{" +
                "id=" + id +
                ", name=" + name +
                ", content=" + content +
                '}';
    }
}
