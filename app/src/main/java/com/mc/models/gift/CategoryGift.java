package com.mc.models.gift;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class CategoryGift implements Serializable {
    @JsonProperty("name")
    private String name;
    @JsonProperty("numberGift")
    private Integer numberGift;
    @JsonProperty("gifts")
    private List<Gift> gifts = null;
    @JsonProperty("id")
    private int id;
    @JsonProperty("giftUserId")
    private int giftUserId;

    public CategoryGift() {

    }

    public CategoryGift(int id, String name, List<Gift> gifts) {
        this.id = id;
        this.name = name;
        this.gifts = gifts;
    }

    public int getGiftUserId() {
        return giftUserId;
    }

    public void setGiftUserId(int giftId) {
        this.giftUserId = giftId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberGift() {
        return numberGift;
    }

    public void setNumberGift(Integer numberGift) {
        this.numberGift = numberGift;
    }

    public List<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(List<Gift> gifts) {
        this.gifts = gifts;
    }

    @Override
    public String toString() {
        return "CategoryGift{" +
                "name='" + name + '\'' +
                ", numberGift=" + numberGift +
                ", gifts=" + gifts +
                ", id=" + id +
                ", giftId=" + giftUserId +
                '}';
    }
}
