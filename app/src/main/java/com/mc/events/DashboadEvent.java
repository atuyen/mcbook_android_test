package com.mc.events;

import com.bon.eventbus.IEvent;

public class DashboadEvent implements IEvent {
    private String message;
    private boolean isShowDialog;

    public boolean isShowDialog() {
        return isShowDialog;
    }

    public void setShowDialog(boolean showDialog) {
        isShowDialog = showDialog;
    }

    public DashboadEvent(String message, boolean isShowDialog) {
        this.message = message;
        this.isShowDialog = isShowDialog;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "DashboadEvent{" +
                "message='" + message + '\'' +
                ", isShowDialog=" + isShowDialog +
                '}';
    }

}
