package com.mc.events;

import com.bon.eventbus.IEvent;

/**
 * Created by HungND on 3/2/18.
 */

public class SignInEvent implements IEvent {
    private String message;

    public SignInEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SignInEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
