package com.mc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ContentResponse implements Serializable {
    @JsonProperty("content")
    private String content;

    public ContentResponse() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ContentResponse{" +
                "content='" + content + '\'' +
                '}';
    }
}
