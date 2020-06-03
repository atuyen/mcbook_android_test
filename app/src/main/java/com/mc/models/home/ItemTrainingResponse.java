package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ItemTrainingResponse implements Serializable {
    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("trainingChapterId")
    private Integer idTraining;
    @JsonProperty("subtitle")
    private String subtitle;
    @JsonProperty("isRead")
    private Boolean isRead;

    public ItemTrainingResponse() {
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Integer getIdTraining() {
        return idTraining;
    }

    public void setIdTraining(Integer idTraining) {
        this.idTraining = idTraining;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ItemTrainingResponse{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", idTraining=" + idTraining +
                ", subtitle='" + subtitle + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
