package com.example.medca.listeners;

import com.example.medca.models.timeslot;

import java.util.List;

public interface ITimeSlotLoadListener {
    void onTimeSlotLoadSuccess (List<timeslot> timeslotList);
    void onTimeSlotLoadFailed (String message);
    void onTimeSlotLoadEmpty();
}
