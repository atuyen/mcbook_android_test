package com.mc.models.gift;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class AddGift implements Serializable{
    @JsonProperty("name")
    private String name;
    @JsonProperty("gifts")
    private List<String> gifts;

    public AddGift() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGifts() {
        return gifts;
    }

    public void setGifts(List<String> gifts) {
        this.gifts = gifts;
    }

    @Override
    public String toString() {
        return "AddGift{" +
                "name='" + name + '\'' +
                ", gifts=" + gifts +
                '}';
    }
}
