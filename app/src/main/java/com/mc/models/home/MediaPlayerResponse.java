package com.mc.models.home;

public class MediaPlayerResponse {
    String url;
    int position;

    public MediaPlayerResponse(String url, int position) {
        this.url = url;
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "MediaPlayerResponse{" +
                "url='" + url + '\'' +
                ", position=" + position +
                '}';
    }
}
