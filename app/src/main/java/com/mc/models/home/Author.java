package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Author implements Serializable {
    @JsonProperty("editor")
    private String name;
    @JsonProperty("picture")
    private String image;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("bookId")
    private Integer bookId;

    public Author() {
    }

    public Author(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", id=" + id +
                ", bookId=" + bookId +
                '}';
    }
}
