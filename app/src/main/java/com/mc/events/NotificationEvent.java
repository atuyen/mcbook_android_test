package com.mc.events;

import com.bon.eventbus.IEvent;

public class NotificationEvent implements IEvent {
   private String event;

    public NotificationEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
