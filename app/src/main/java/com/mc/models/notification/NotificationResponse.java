package com.mc.models.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class NotificationResponse {
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("rows")
    private List<Notification> notificationList = null;
    @JsonProperty("totalBaggy")
    private Integer totalBaggy;


    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("rows")
    public List<Notification> getNotificationList() {
        return notificationList;
    }

    @JsonProperty("rows")
    public void setNotificationList(List<Notification> rows) {
        this.notificationList = rows;
    }

    @JsonProperty("totalBaggy")
    public Integer getTotalBaggy() {
        return totalBaggy;
    }

    @JsonProperty("totalBaggy")
    public void setTotalBaggy(Integer totalBaggy) {
        this.totalBaggy = totalBaggy;
    }

    @Override
    public String toString() {
        return "NotificationResponse{" +
                "count=" + count +
                ", notificationList=" + notificationList +
                ", totalBaggy=" + totalBaggy +
                '}';
    }
}
