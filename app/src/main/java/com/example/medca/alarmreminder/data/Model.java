package com.example.medca.alarmreminder.data;

//model class is used to set and get the data from database

import com.example.medca.models.User;

public class Model extends User {
    String title, date, time;

    public Model() {
    }

    public Model(String title, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
