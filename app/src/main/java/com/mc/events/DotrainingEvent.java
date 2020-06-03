package com.mc.events;

import com.bon.eventbus.IEvent;

public class DotrainingEvent implements IEvent {
    private boolean isShowDialog;

    public boolean isShowDialog() {
        return isShowDialog;
    }

    public void setShowDialog(boolean showDialog) {
        isShowDialog = showDialog;
    }

    public DotrainingEvent(boolean isShowDialog) {
        this.isShowDialog = isShowDialog;
    }

    @Override
    public String toString() {
        return "DotrainingEvent{" +
                "isShowDialog=" + isShowDialog +
                '}';
    }
}
