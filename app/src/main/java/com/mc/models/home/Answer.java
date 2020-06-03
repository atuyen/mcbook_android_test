package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Answer implements Serializable {
    @JsonProperty("correct")
    private String correct;
    @JsonProperty("content")
    private String content;
    @JsonProperty("left")
    private Mathching left;
    @JsonProperty("right")
    private Mathching right;
    private boolean isMatching;
    private int postion;
    private boolean isAnimation;

    //
    @JsonProperty("id")
    private String id;
    @JsonProperty("text")
    private String text;
    @JsonProperty("isCorrect")
    private Boolean isCorrect;
    @JsonProperty("mediaUri")
    private String mediaUri;
    @JsonProperty("thumbUri")
    private String thumbUri;
    @JsonProperty("typeMedia")
    private String typeMedia;
    @JsonProperty("temp")
    private String temp;

    private String fillWordAnswer;
    private boolean isChoose;

    public Answer() {
    }

    public String getThumbUri() {
        return thumbUri;
    }

    public void setThumbUri(String thumbUri) {
        this.thumbUri = thumbUri;
    }

    public boolean isAnimation() {
        return isAnimation;
    }

    public void setAnimation(boolean animation) {
        isAnimation = animation;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public Answer(Mathching left, Mathching right) {
        this.left = left;
        this.right = right;
    }

    public boolean isMatching() {
        return isMatching;
    }

    public void setMatching(boolean matching) {
        isMatching = matching;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTypeMedia() {
        return typeMedia;
    }

    public void setTypeMedia(String typeMedia) {
        this.typeMedia = typeMedia;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getFillWordAnswer() {
        return fillWordAnswer;
    }

    public void setFillWordAnswer(String fillWordAnswer) {
        this.fillWordAnswer = fillWordAnswer;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Mathching getLeft() {
        return left;
    }

    public void setLeft(Mathching left) {
        this.left = left;
    }

    public Mathching getRight() {
        return right;
    }

    public void setRight(Mathching right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "correct='" + correct + '\'' +
                ", content='" + content + '\'' +
                ", left=" + left +
                ", right=" + right +
                ", isMatching=" + isMatching +
                ", postion=" + postion +
                ", isAnimation=" + isAnimation +
                ", id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", isCorrect=" + isCorrect +
                ", mediaUri='" + mediaUri + '\'' +
                ", thumbUri='" + thumbUri + '\'' +
                ", typeMedia='" + typeMedia + '\'' +
                ", temp='" + temp + '\'' +
                ", fillWordAnswer='" + fillWordAnswer + '\'' +
                ", isChoose=" + isChoose +
                '}';
    }
}
