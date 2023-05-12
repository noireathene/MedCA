package com.example.medca.activities;

//model class is used to set and get the data from database

public class PatientView {
    String name, age, gender;

    public PatientView() {
    }

    public PatientView(String name, String age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setGender(String gender) { this.gender = gender;
    }
}
