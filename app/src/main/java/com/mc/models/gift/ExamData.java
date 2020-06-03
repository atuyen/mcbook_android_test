package com.mc.models.gift;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ExamData implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("level")
    private String level;
    @JsonProperty("subjectName")
    private String subjectName;
    @JsonProperty("time")
    private Integer time;

    public ExamData() {
    }

    public ExamData(Integer id) {
        this.id = id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "ExamData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", subjectName='" + subjectName + '\'' +
                '}';
    }
}
