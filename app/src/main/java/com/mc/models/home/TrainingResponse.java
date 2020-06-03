package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TrainingResponse {
    @JsonProperty("name")
    private String name;
    @JsonProperty("trainingLessons")
    private List<ItemTrainingResponse> trainings = null;
    @JsonProperty("id")
    private int id;
    @JsonProperty("isRead")
    private Boolean isRead;

    public TrainingResponse(String name, List<ItemTrainingResponse> trainings, int id) {
        this.name = name;
        this.trainings = trainings;
        this.id = id;
    }

    public TrainingResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemTrainingResponse> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<ItemTrainingResponse> trainings) {
        this.trainings = trainings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "TrainingResponse{" +
                "name='" + name + '\'' +
                ", trainings=" + trainings +
                ", id=" + id +
                ", isRead=" + isRead +
                '}';
    }
}
