package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DetailTraining {
    @JsonProperty("audioLesson")
    private String audioLesson;
    @JsonProperty("subtitle")
    private String subtitle;
    @JsonProperty("id")
    private int id;
    @JsonProperty("bookId")
    private int bookId;
    @JsonProperty("trainingChapterId")
    private int trainingChapterId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("numberRoles")
    private int numberRoles;
    @JsonProperty("active")
    private boolean active;
    @JsonProperty("order")
    private int order;
    @JsonProperty("trainingAudios")
    private List<TrainingAudio> trainingAudios = null;

    public DetailTraining() {
    }

    public String getAudioLesson() {
        return audioLesson;
    }

    public void setAudioLesson(String audioLesson) {
        this.audioLesson = audioLesson;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getTrainingChapterId() {
        return trainingChapterId;
    }

    public void setTrainingChapterId(int trainingChapterId) {
        this.trainingChapterId = trainingChapterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberRoles() {
        return numberRoles;
    }

    public void setNumberRoles(int numberRoles) {
        this.numberRoles = numberRoles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<TrainingAudio> getTrainingAudios() {
        return trainingAudios;
    }

    public void setTrainingAudios(List<TrainingAudio> trainingAudios) {
        this.trainingAudios = trainingAudios;
    }

    @Override
    public String toString() {
        return "DetailTraining{" +
                "audioLesson='" + audioLesson + '\'' +
                ", subtitle=" + subtitle +
                ", id=" + id +
                ", bookId=" + bookId +
                ", trainingChapterId=" + trainingChapterId +
                ", name='" + name + '\'' +
                ", numberRoles=" + numberRoles +
                ", active=" + active +
                ", order=" + order +
                ", trainingAudios=" + trainingAudios +
                '}';
    }
}
