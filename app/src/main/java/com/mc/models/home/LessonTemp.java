package com.mc.models.home;

import java.util.List;

public class LessonTemp {
    private int index;
    private List<Lesson> lessons;
    private Lesson lesson;

    public LessonTemp() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    @Override
    public String toString() {
        return "LessonTemp{" +
                "index=" + index +
                ", lessons=" + lessons +
                ", lesson=" + lesson +
                '}';
    }
}
