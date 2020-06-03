package com.mc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MessageResponse implements Serializable {
    @JsonProperty("message")
    private String message;

    public MessageResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
