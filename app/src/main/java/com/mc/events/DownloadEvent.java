package com.mc.events;

import com.bon.eventbus.IEvent;

public class DownloadEvent implements IEvent {
    int index;
    int bookId;

    public DownloadEvent(int index, int bookId) {
        this.index = index;
        this.bookId = bookId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
