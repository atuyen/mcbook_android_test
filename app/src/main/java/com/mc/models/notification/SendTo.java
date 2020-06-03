package com.mc.models.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SendTo {
    @JsonProperty("age")
    private List<String> age = null;
    @JsonProperty("typeUser")
    private List<String> typeUser = null;
    @JsonProperty("condition")
    private Condition condition;

    @JsonProperty("age")
    public List<String> getAge() {
        return age;
    }

    @JsonProperty("age")
    public void setAge(List<String> age) {
        this.age = age;
    }

    @JsonProperty("typeUser")
    public List<String> getTypeUser() {
        return typeUser;
    }

    @JsonProperty("typeUser")
    public void setTypeUser(List<String> typeUser) {
        this.typeUser = typeUser;
    }

    @JsonProperty("condition")
    public Condition getCondition() {
        return condition;
    }

    @JsonProperty("condition")
    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
