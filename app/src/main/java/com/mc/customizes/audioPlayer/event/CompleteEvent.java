package com.mc.customizes.audioPlayer.event;

public class CompleteEvent {
    private String message;

    public CompleteEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
