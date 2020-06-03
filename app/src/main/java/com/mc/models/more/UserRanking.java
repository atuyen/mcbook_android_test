package com.mc.models.more;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class UserRanking implements Serializable {
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
    private String lastName;
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
    @JsonProperty("job")
    private String job;
    @JsonProperty("fullname")
    private String fullname;
    @JsonProperty("district")
    private String district;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("cv")
    private String cv;
    @JsonProperty("updatedTimestamp")
    private String updatedTimestamp;
    @JsonProperty("expertise")
    private String expertise;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("description")
    private String description;
    @JsonProperty("city")
    private String city;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("address")
    private String address;
    @JsonProperty("role")
    private String role;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("point")
    private String point;
    @JsonProperty("rank")
    private Integer rank;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserRanking{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", emailConstraint='" + emailConstraint + '\'' +
                ", emailVerified=" + emailVerified +
                ", enabled=" + enabled +
                ", federationLink=" + federationLink +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", realmId='" + realmId + '\'' +
                ", username='" + username + '\'' +
                ", createdTimestamp='" + createdTimestamp + '\'' +
                ", serviceAccountClientLink=" + serviceAccountClientLink +
                ", notBefore=" + notBefore +
                ", attributes=" + attributes +
                ", job='" + job + '\'' +
                ", fullname='" + fullname + '\'' +
                ", district='" + district + '\'' +
                ", phone='" + phone + '\'' +
                ", cv='" + cv + '\'' +
                ", updatedTimestamp='" + updatedTimestamp + '\'' +
                ", expertise='" + expertise + '\'' +
                ", avatar='" + avatar + '\'' +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                ", birthday='" + birthday + '\'' +
                ", point='" + point + '\'' +
                ", rank=" + rank +
                '}';
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("emailConstraint")
    public String getEmailConstraint() {
        return emailConstraint;
    }

    @JsonProperty("emailConstraint")
    public void setEmailConstraint(String emailConstraint) {
        this.emailConstraint = emailConstraint;
    }

    @JsonProperty("emailVerified")
    public Boolean getEmailVerified() {
        return emailVerified;
    }

    @JsonProperty("emailVerified")
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @JsonProperty("enabled")
    public Boolean getEnabled() {
        return enabled;
    }

    @JsonProperty("enabled")
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty("federationLink")
    public Object getFederationLink() {
        return federationLink;
    }

    @JsonProperty("federationLink")
    public void setFederationLink(Object federationLink) {
        this.federationLink = federationLink;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("realmId")
    public String getRealmId() {
        return realmId;
    }

    @JsonProperty("realmId")
    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("createdTimestamp")
    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    @JsonProperty("createdTimestamp")
    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @JsonProperty("serviceAccountClientLink")
    public Object getServiceAccountClientLink() {
        return serviceAccountClientLink;
    }

    @JsonProperty("serviceAccountClientLink")
    public void setServiceAccountClientLink(Object serviceAccountClientLink) {
        this.serviceAccountClientLink = serviceAccountClientLink;
    }

    @JsonProperty("notBefore")
    public Integer getNotBefore() {
        return notBefore;
    }

    @JsonProperty("notBefore")
    public void setNotBefore(Integer notBefore) {
        this.notBefore = notBefore;
    }

    @JsonProperty("attributes")
    public Object getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }

    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    @JsonProperty("job")
    public void setJob(String job) {
        this.job = job;
    }

    @JsonProperty("fullname")
    public String getFullname() {
        return fullname;
    }

    @JsonProperty("fullname")
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @JsonProperty("district")
    public String getDistrict() {
        return district;
    }

    @JsonProperty("district")
    public void setDistrict(String district) {
        this.district = district;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("cv")
    public String getCv() {
        return cv;
    }

    @JsonProperty("cv")
    public void setCv(String cv) {
        this.cv = cv;
    }

    @JsonProperty("updatedTimestamp")
    public String getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    @JsonProperty("updatedTimestamp")
    public void setUpdatedTimestamp(String updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    @JsonProperty("expertise")
    public String getExpertise() {
        return expertise;
    }

    @JsonProperty("expertise")
    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    @JsonProperty("avatar")
    public String getAvatar() {
        return avatar;
    }

    @JsonProperty("avatar")
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    @JsonProperty("role")
    public void setRole(String role) {
        this.role = role;
    }

    @JsonProperty("birthday")
    public String getBirthday() {
        return birthday;
    }

    @JsonProperty("birthday")
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @JsonProperty("point")
    public String getPoint() {
        return point;
    }

    @JsonProperty("point")
    public void setPoint(String point) {
        this.point = point;
    }

    @JsonProperty("rank")
    public Integer getRank() {
        return rank;
    }

    @JsonProperty("rank")
    public void setRank(Integer rank) {
        this.rank = rank;
    }

}
