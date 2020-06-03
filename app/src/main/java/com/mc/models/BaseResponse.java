package com.mc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {
    @JsonProperty("data")
    private T data;
    @JsonProperty("message")
    private String message;

    public BaseResponse() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
