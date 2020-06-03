package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SendLog implements Serializable {
    @JsonProperty("turn")
    private String turn;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("questionId")
    private Integer questionId;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("answer")
    private Boolean answer;
    @JsonProperty("bookId")
    private Integer bookId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("submitAnswer")
    private String submitAnswer;

    public SendLog() {
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubmitAnswer() {
        return submitAnswer;
    }

    public void setSubmitAnswer(String submitAnswer) {
        this.submitAnswer = submitAnswer;
    }

    @Override
    public String toString() {
        return "SendLog{" +
                "turn='" + turn + '\'' +
                ", id=" + id +
                ", questionId=" + questionId +
                ", userId='" + userId + '\'' +
                ", answer=" + answer +
                ", bookId=" + bookId +
                ", type='" + type + '\'' +
                ", submitAnswer='" + submitAnswer + '\'' +
                '}';
    }
}
