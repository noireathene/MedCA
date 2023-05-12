package com.example.medca.listeners;

import android.view.View;

import com.example.medca.activities.MedicineModal;

import java.util.ArrayList;

public interface MedicineListener {
    public void onClick(View view, int position);

    void onClick(ArrayList<MedicineModal> medicines);
}
