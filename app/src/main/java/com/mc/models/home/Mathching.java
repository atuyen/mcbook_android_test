package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Mathching implements Serializable {
    @JsonProperty("text")
    private String text;
    @JsonProperty("mediaUri")
    private String mediaUri;
    @JsonProperty("key")
    private String key;

    public Mathching() {
    }

    public Mathching(String text, String mediaUri) {
        this.text = text;
        this.mediaUri = mediaUri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }

    @Override
    public String toString() {
        return "Mathching{" +
                "text='" + text + '\'' +
                ", mediaUri='" + mediaUri + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
