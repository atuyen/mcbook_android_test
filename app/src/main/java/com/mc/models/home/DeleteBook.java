package com.mc.models.home;

import android.view.View;

import java.io.Serializable;

public class DeleteBook implements Serializable {

    private BookResponse bookResponse;
    private View view;

    public DeleteBook() {
    }

    public DeleteBook(BookResponse bookResponse, View view) {
        this.bookResponse = bookResponse;
        this.view = view;
    }

    public BookResponse getBookResponse() {
        return bookResponse;
    }

    public void setBookResponse(BookResponse bookResponse) {
        this.bookResponse = bookResponse;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public String toString() {
        return "DeleteBook{" +
                "bookResponse=" + bookResponse +
                ", view=" + view +
                '}';
    }
}
