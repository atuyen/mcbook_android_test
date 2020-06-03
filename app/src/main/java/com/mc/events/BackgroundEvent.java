package com.mc.events;

import com.bon.eventbus.IEvent;

public class BackgroundEvent implements IEvent {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BackgroundEvent(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BackgroundEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
