package com.mc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Upload implements Serializable{
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("path")
    private String path;

    public Upload() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Upload{" +
                "uri='" + uri + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
