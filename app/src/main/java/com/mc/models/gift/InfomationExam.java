package com.mc.models.gift;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class InfomationExam implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("numberQuestions")
    private Object numberQuestions;
    @JsonProperty("avatarUri")
    private String avatarUri;
    @JsonProperty("time")
    private Integer time;
    @JsonProperty("level")
    private String level;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("updatedBy")
    private String updatedBy;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("deleted_at")
    private Object deletedAt;
    @JsonProperty("subject_id")
    private Object subjectId;
    @JsonProperty("classIds")
    private List<Integer> classIds = null;
    @JsonProperty("subjectName")
    private String subjectName;
    @JsonProperty("numberOfExams")
    private Integer numberOfExams;
    @JsonProperty("maxExams")
    private Integer maxExams;
    @JsonProperty("histories")
    private List<HistoryExam> histories = null;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("total")
    public Integer getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(Integer total) {
        this.total = total;
    }

    @JsonProperty("numberQuestions")
    public Object getNumberQuestions() {
        return numberQuestions;
    }

    @JsonProperty("numberQuestions")
    public void setNumberQuestions(Object numberQuestions) {
        this.numberQuestions = numberQuestions;
    }

    @JsonProperty("avatarUri")
    public String getAvatarUri() {
        return avatarUri;
    }

    @JsonProperty("avatarUri")
    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    @JsonProperty("time")
    public Integer getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(Integer time) {
        this.time = time;
    }

    @JsonProperty("level")
    public String getLevel() {
        return level;
    }

    @JsonProperty("level")
    public void setLevel(String level) {
        this.level = level;
    }

    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("updatedBy")
    public String getUpdatedBy() {
        return updatedBy;
    }

    @JsonProperty("updatedBy")
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("deleted_at")
    public Object getDeletedAt() {
        return deletedAt;
    }

    @JsonProperty("deleted_at")
    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    @JsonProperty("subject_id")
    public Object getSubjectId() {
        return subjectId;
    }

    @JsonProperty("subject_id")
    public void setSubjectId(Object subjectId) {
        this.subjectId = subjectId;
    }

    @JsonProperty("classIds")
    public List<Integer> getClassIds() {
        return classIds;
    }

    @JsonProperty("classIds")
    public void setClassIds(List<Integer> classIds) {
        this.classIds = classIds;
    }

    @JsonProperty("subjectName")
    public String getSubjectName() {
        return subjectName;
    }

    @JsonProperty("subjectName")
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @JsonProperty("numberOfExams")
    public Integer getNumberOfExams() {
        return numberOfExams;
    }

    @JsonProperty("numberOfExams")
    public void setNumberOfExams(Integer numberOfExams) {
        this.numberOfExams = numberOfExams;
    }

    @JsonProperty("maxExams")
    public Integer getMaxExams() {
        return maxExams;
    }

    @JsonProperty("maxExams")
    public void setMaxExams(Integer maxExams) {
        this.maxExams = maxExams;
    }

    @JsonProperty("histories")
    public List<HistoryExam> getHistories() {
        return histories;
    }

    @JsonProperty("histories")
    public void setHistories(List<HistoryExam> histories) {
        this.histories = histories;
    }
}
