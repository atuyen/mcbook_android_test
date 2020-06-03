//package com.mc.models.home;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//import java.io.Serializable;
//import java.util.List;
//
//public class Subject implements Serializable {
//    @JsonProperty("id")
//    private Integer id;
//    @JsonProperty("name")
//    private String name;
//    @JsonProperty("active")
//    private Boolean active;
//    @JsonProperty("questions")
//    private List<Question> questions = null;
//
//    public Subject() {
//    }
//
//    public Subject(Integer id, String name, Boolean active, Point point, List<Question> questions) {
//        this.id = id;
//        this.name = name;
//        this.active = active;
//        this.point = point;
//        this.questions = questions;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Boolean getActive() {
//        return active;
//    }
//
//    public void setActive(Boolean active) {
//        this.active = active;
//    }
//
//    public Point getPoint() {
//        return point;
//    }
//
//    public void setPoint(Point point) {
//        this.point = point;
//    }
//
//    public List<Question> getQuestions() {
//        return questions;
//    }
//
//    public void setQuestions(List<Question> questions) {
//        this.questions = questions;
//    }
//
//    @Override
//    public String toString() {
//        return "Subject{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", active=" + active +
//                ", point=" + point +
//                ", questions=" + questions +
//                '}';
//    }
//}
