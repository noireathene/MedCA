package com.example.medca.activities;

public class MedicineModal {
    // variables for our course
    // name and description.
    private String medicineName;
    private String genName;

    // creating constructor for our variables.
    public MedicineModal(String medicineName, String genName) {
        this.medicineName = medicineName;
        this.genName = genName;
    }

    // creating getter and setter methods.
    public String getMedicineName() {
        return medicineName;
    }
    public String getGenName() {
        return genName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

 }


