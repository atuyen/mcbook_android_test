package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class RelatedBook implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("avatar")
    private String picture;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("webUri")
    private String uri;
    @JsonProperty("description")
    private String description;
    @JsonProperty("author")
    private List<Author> authorList;


    public RelatedBook() {
    }

    public RelatedBook(Integer id, String name, String picture, Integer price, String uri) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.price = price;
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "RelatedBook{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                ", uri='" + uri + '\'' +
                ", description='" + description + '\'' +
                ", authorList=" + authorList +
                '}';
    }
}
