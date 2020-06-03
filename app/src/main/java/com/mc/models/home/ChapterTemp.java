package com.mc.models.home;

public class ChapterTemp {
    private Chapter chapter;
    private int index;

    public ChapterTemp(Chapter chapter, int index) {
        this.chapter = chapter;
        this.index = index;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "ChapterTemp{" +
                "chapter=" + chapter +
                ", index=" + index +
                '}';
    }
}
