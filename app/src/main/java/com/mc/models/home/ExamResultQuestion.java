package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class ExamResultQuestion implements Serializable {
    @JsonProperty("id")
    private Integer id;
    private boolean isCorrect;

    public ExamResultQuestion() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return "ExamResultQuestion{" +
                "id=" + id +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
