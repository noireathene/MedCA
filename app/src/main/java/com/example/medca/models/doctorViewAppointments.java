package com.example.medca.models;

public class doctorViewAppointments {
    public String patientName;
    public String appointDate;
    public String appointTime;
    public String appointReason;
    public String docName;
    public String documentID;
    public String patientId;
    public doctorViewAppointments() {
    }

    public doctorViewAppointments(String patientName, String appointDate, String appointTime, String appointReason, String docname, String patientId) {
        this.patientName = patientName;
        this.appointDate = appointDate;
        this.appointTime = appointTime;
        this.appointReason = appointReason;
        this.docName = docname;
        this.patientId = patientId;
    }



    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(String appointDate) {
        this.appointDate = appointDate;
    }

    public String getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }

    public String getDoctor() {
        return docName;
    }

    public void setAppointReason(String appointReason) {
        this.appointReason = appointReason;
    }

    public String getAppointReason() {
        return appointReason;
    }

    public void setDoctor(String reason) {
        this.docName = reason;
    }

    public String getPatientID() {
        return patientId;
    }

    public void setPatientID(String reason) {
        this.patientId = reason;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}
