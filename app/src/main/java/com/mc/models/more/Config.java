package com.mc.models.more;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Config implements Serializable {
    @JsonProperty("phone")
    String phone;
    @JsonProperty("facebook")
    String facebook;
    @JsonProperty("androidURL")
    String androidURL;

    public Config() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getAndroidURL() {
        return androidURL;
    }

    public void setAndroidURL(String androidURL) {
        this.androidURL = androidURL;
    }

    @Override
    public String toString() {
        return "Config{" +
                "phone='" + phone + '\'' +
                ", facebook='" + facebook + '\'' +
                ", androidURL='" + androidURL + '\'' +
                '}';
    }
}
