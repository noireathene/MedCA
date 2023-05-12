package com.example.medca.models;

import java.io.Serializable;

public class User implements Serializable {

    public String name, email, token, id, age, gender, diagnosis, medicine, intake, schedule, status, time, reason;


    public User() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDiagnosis() { return diagnosis; }

    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getMedicine() { return medicine; }

    public void setMedicine(String medicine) { this.medicine = medicine; }

    public String getIntake() { return intake; }

    public void setIntake(String intake) { this.intake = intake; }

    public String getSchedule() { return schedule; }

    public void setSchedule(String schedule) { this.schedule = schedule; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

}