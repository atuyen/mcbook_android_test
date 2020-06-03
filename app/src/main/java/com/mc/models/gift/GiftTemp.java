package com.mc.models.gift;

import java.util.List;

public class GiftTemp {
    private List<Gift> gifts;
    private Gift gift;
    private int index;
    private int id;
    private int giftUserId;

    public GiftTemp() {
    }

    public int getGiftUserId() {
        return giftUserId;
    }

    public void setGiftUserId(int giftUserId) {
        this.giftUserId = giftUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(List<Gift> gifts) {
        this.gifts = gifts;
    }

    public Gift getGift() {
        return gift;
    }

    public void setGift(Gift gift) {
        this.gift = gift;
    }

    @Override
    public String toString() {
        return "GiftTemp{" +
                "gifts=" + gifts +
                ", gift=" + gift +
                ", index=" + index +
                ", id=" + id +
                '}';
    }
}
