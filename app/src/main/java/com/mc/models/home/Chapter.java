package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Chapter implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("lessons")
    private List<Lesson> lessons;
    private int index;
    private boolean isExpand = false;
    private boolean isClickExpand = false;
    private int chaptermaxsize;

    public Chapter() {
    }

    public Chapter(Integer id, String name, List<Lesson> lessons) {
        this.id = id;
        this.name = name;
        this.lessons = lessons;
    }

    public Chapter(Integer id, String name, List<Lesson> lessons, int idx) {
        this.id = id;
        this.name = name;
        this.lessons = lessons;
        this.index = idx;
    }

    public int getChaptermaxsize() {
        return chaptermaxsize;
    }

    public void setChaptermaxsize(int chaptermaxsize) {
        this.chaptermaxsize = chaptermaxsize;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    public boolean isClickExpand() {
        return isClickExpand;
    }

    public void setIsClickExpand(boolean isClickExpand) {
        this.isClickExpand = isClickExpand;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lessons=" + lessons +
                '}';
    }
}
