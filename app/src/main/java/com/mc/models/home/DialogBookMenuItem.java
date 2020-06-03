package com.mc.models.home;


import java.io.Serializable;

public class DialogBookMenuItem implements Serializable {
    private String title;

    public DialogBookMenuItem(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getTitle() {
        return this.title;
    }
}
