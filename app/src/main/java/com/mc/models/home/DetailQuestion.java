package com.mc.models.home;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class DetailQuestion implements Serializable {
    @JsonProperty("descriptionMediaUri")
    private String descriptionMediaUri;
    @JsonProperty("descriptionSubUri")
    private String descriptionSubUri;
    @JsonProperty("contentMediaUri")
    private String contentMediaUri;
    @JsonProperty("contentSubUri")
    private String contentSubUri;
    @JsonProperty("noteMediaUri")
    private String noteMediaUri;
    @JsonProperty("noteSubUri")
    private String noteSubUri;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("level")
    private String level;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("content")
    private String content;
    @JsonProperty("note")
    private String note;
    @JsonProperty("type")
    private String type;
    @JsonProperty("descriptionMediaType")
    private String descriptionMediaType;
    @JsonProperty("noteMediaType")
    private String noteMediaType;
    @JsonProperty("contentMediaType")
    private String contentMediaType;
    @JsonProperty("answers")
    private List<Answer> answers;
    @JsonProperty("answerRandom")
    private List<Answer> answerRandom;
    @JsonProperty("descriptionQuestionForms")
    private String descriptionQuestionForms;
    @JsonProperty("submitAnswer")
    private String submitAnswer;
    @JsonProperty("isSubmit")
    private boolean isSubmit;

    private boolean isAnswer;

    private boolean isCorrect;

    private Integer SingleChoosePosition = null;

    public DetailQuestion() {
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public Integer getSingleChoosePosition() {
        return SingleChoosePosition;
    }

    public void setSingleChoosePosition(Integer singleChoosePosition) {
        SingleChoosePosition = singleChoosePosition;
    }

    public List<Answer> getAnswerRandom() {
        return answerRandom;
    }

    public void setAnswerRandom(List<Answer> answerRandom) {
        this.answerRandom = answerRandom;
    }

    public String getDescriptionQuestionForms() {
        return descriptionQuestionForms;
    }

    public void setDescriptionQuestionForms(String descriptionQuestionForms) {
        this.descriptionQuestionForms = descriptionQuestionForms;
    }

    public String getSubmitAnswer() {
        return submitAnswer;
    }

    public void setSubmitAnswer(String submitAnswer) {
        this.submitAnswer = submitAnswer;
    }

    public boolean isSubmit() {
        return isSubmit;
    }

    public void setSubmit(boolean submit) {
        isSubmit = submit;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getDescriptionMediaUri() {
        return descriptionMediaUri;
    }

    public void setDescriptionMediaUri(String descriptionMediaUri) {
        this.descriptionMediaUri = descriptionMediaUri;
    }

    public String getDescriptionSubUri() {
        return descriptionSubUri;
    }

    public void setDescriptionSubUri(String descriptionSubUri) {
        this.descriptionSubUri = descriptionSubUri;
    }

    public String getContentMediaUri() {
        return contentMediaUri;
    }

    public void setContentMediaUri(String contentMediaUri) {
        this.contentMediaUri = contentMediaUri;
    }

    public String getContentSubUri() {
        return contentSubUri;
    }

    public void setContentSubUri(String contentSubUri) {
        this.contentSubUri = contentSubUri;
    }

    public String getNoteMediaUri() {
        return noteMediaUri;
    }

    public void setNoteMediaUri(String noteMediaUri) {
        this.noteMediaUri = noteMediaUri;
    }

    public String getNoteSubUri() {
        return noteSubUri;
    }

    public void setNoteSubUri(String noteSubUri) {
        this.noteSubUri = noteSubUri;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescriptionMediaType() {
        return descriptionMediaType;
    }

    public void setDescriptionMediaType(String descriptionMediaType) {
        this.descriptionMediaType = descriptionMediaType;
    }

    public String getNoteMediaType() {
        return noteMediaType;
    }

    public void setNoteMediaType(String noteMediaType) {
        this.noteMediaType = noteMediaType;
    }

    public String getContentMediaType() {
        return contentMediaType;
    }

    public void setContentMediaType(String contentMediaType) {
        this.contentMediaType = contentMediaType;
    }

    @Override
    public String toString() {
        return "DetailQuestion{" +
                "descriptionMediaUri='" + descriptionMediaUri + '\'' +
                ", descriptionSubUri='" + descriptionSubUri + '\'' +
                ", contentMediaUri='" + contentMediaUri + '\'' +
                ", contentSubUri='" + contentSubUri + '\'' +
                ", noteMediaUri='" + noteMediaUri + '\'' +
                ", noteSubUri='" + noteSubUri + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", level='" + level + '\'' +
                ", active=" + active +
                ", content='" + content + '\'' +
                ", note='" + note + '\'' +
                ", type='" + type + '\'' +
                ", descriptionMediaType='" + descriptionMediaType + '\'' +
                ", noteMediaType='" + noteMediaType + '\'' +
                ", contentMediaType='" + contentMediaType + '\'' +
                ", answers=" + answers +
                ", answerRandom=" + answerRandom +
                ", descriptionQuestionForms='" + descriptionQuestionForms + '\'' +
                ", submitAnswer='" + submitAnswer + '\'' +
                ", isSubmit=" + isSubmit +
                ", isAnswer=" + isAnswer +
                ", isCorrect=" + isCorrect +
                ", SingleChoosePosition=" + SingleChoosePosition +
                '}';
    }
}
