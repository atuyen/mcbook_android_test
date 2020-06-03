package com.bon.customview.keyvaluepair;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.bon.util.StringUtils;

import java.io.Serializable;

/**
 * Created by Dang Pham Phu on 2/2/2017.
 */

public class ExtKeyValuePair implements Serializable {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("Value")
    private String value;
    @JsonIgnore
    private boolean selected;

    public ExtKeyValuePair() {
    }

    public ExtKeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public ExtKeyValuePair(String key, String value, boolean selected) {
        this.key = key;
        this.value = value;
        this.selected = selected;
    }

    public String getKey() {
        return StringUtils.isEmpty(key) ? "" : key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return StringUtils.isEmpty(value) ? "" : value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "ExtKeyValuePair{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", selected=" + selected +
                '}';
    }
}
