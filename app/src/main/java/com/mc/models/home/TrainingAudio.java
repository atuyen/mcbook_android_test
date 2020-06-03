package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TrainingAudio implements Serializable {
    @JsonProperty("audioFile")
    private String audioFile;
    @JsonProperty("id")
    private int id;
    @JsonProperty("trainingLessonId")
    private int trainingLessonId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("hashtag")
    private String hashtag;

    public TrainingAudio() {
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrainingLessonId() {
        return trainingLessonId;
    }

    public void setTrainingLessonId(int trainingLessonId) {
        this.trainingLessonId = trainingLessonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    @Override
    public String toString() {
        return "TrainingAudio{" +
                "audioFile='" + audioFile + '\'' +
                ", id=" + id +
                ", trainingLessonId=" + trainingLessonId +
                ", name='" + name + '\'' +
                ", hashtag='" + hashtag + '\'' +
                '}';
    }
}
