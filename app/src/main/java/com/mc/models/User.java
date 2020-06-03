package com.mc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class User implements Serializable {
    @JsonProperty("id")
    private String id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("emailConstraint")
    private String emailConstraint;
    @JsonProperty("emailVerified")
    private Boolean emailVerified;
    @JsonProperty("enabled")
    private Boolean enabled;
    @JsonProperty("federationLink")
    private Object federationLink;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private Object lastName;
    @JsonProperty("realmId")
    private String realmId;
    @JsonProperty("username")
    private String username;
    @JsonProperty("createdTimestamp")
    private String createdTimestamp;
    @JsonProperty("serviceAccountClientLink")
    private Object serviceAccountClientLink;
    @JsonProperty("notBefore")
    private Integer notBefore;
    @JsonProperty("attributes")
    private Object attributes;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("role")
    private String role;
    @JsonProperty("address")
    private String address;
    @JsonProperty("cv")
    private String cv;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("description")
    private String description;
    @JsonProperty("fullname")
    private String fullname;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("expertise")
    private String expertise;
    @JsonProperty("job")
    private String job;
    @JsonProperty("updatedTimestamp")
    private String updatedTimestamp;
    @JsonProperty("cvRoot")
    private String cvRoot;
    @JsonProperty("avatarRoot")
    private String avatarRoot;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("city")
    private String city;
    @JsonProperty("district")
    private String district;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailConstraint() {
        return emailConstraint;
    }

    public void setEmailConstraint(String emailConstraint) {
        this.emailConstraint = emailConstraint;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Object getFederationLink() {
        return federationLink;
    }

    public void setFederationLink(Object federationLink) {
        this.federationLink = federationLink;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Object getServiceAccountClientLink() {
        return serviceAccountClientLink;
    }

    public void setServiceAccountClientLink(Object serviceAccountClientLink) {
        this.serviceAccountClientLink = serviceAccountClientLink;
    }

    public Integer getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Integer notBefore) {
        this.notBefore = notBefore;
    }

    public Object getAttributes() {
        return attributes;
    }

    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(String updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public String getCvRoot() {
        return cvRoot;
    }

    public void setCvRoot(String cvRoot) {
        this.cvRoot = cvRoot;
    }

    public String getAvatarRoot() {
        return avatarRoot;
    }

    public void setAvatarRoot(String avatarRoot) {
        this.avatarRoot = avatarRoot;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", emailConstraint='" + emailConstraint + '\'' +
                ", emailVerified=" + emailVerified +
                ", enabled=" + enabled +
                ", federationLink=" + federationLink +
                ", firstName='" + firstName + '\'' +
                ", lastName=" + lastName +
                ", realmId='" + realmId + '\'' +
                ", username='" + username + '\'' +
                ", createdTimestamp='" + createdTimestamp + '\'' +
                ", serviceAccountClientLink=" + serviceAccountClientLink +
                ", notBefore=" + notBefore +
                ", attributes=" + attributes +
                ", birthday='" + birthday + '\'' +
                ", role='" + role + '\'' +
                ", address='" + address + '\'' +
                ", cv='" + cv + '\'' +
                ", phone='" + phone + '\'' +
                ", description='" + description + '\'' +
                ", fullname='" + fullname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", expertise='" + expertise + '\'' +
                ", job='" + job + '\'' +
                ", updatedTimestamp='" + updatedTimestamp + '\'' +
                ", cvRoot='" + cvRoot + '\'' +
                ", avatarRoot='" + avatarRoot + '\'' +
                ", gender=" + gender +
                ", city=" + city +
                ", district=" + district +
                '}';
    }
}
