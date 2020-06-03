package com.mc.models.gift;

import java.io.Serializable;

public class Question implements Serializable {

    private String nameQuestion;
    private int type;

    public Question(String nameQuestion, int type) {
        this.nameQuestion = nameQuestion;
        this.type = type;
    }

    public Question() {
    }

    public String getNameQuestion() {
        return nameQuestion;
    }

    public void setNameQuestion(String nameQuestion) {
        this.nameQuestion = nameQuestion;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
