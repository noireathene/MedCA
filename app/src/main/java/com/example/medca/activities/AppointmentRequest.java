package com.example.medca.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.medca.R;
import com.example.medca.adapters.BookingAdapter;
import com.example.medca.adapters.DoctorAppointmentAdapter;
import com.example.medca.databinding.ActivityAppointmentRequestBinding;
import com.example.medca.databinding.ActivityBookingsListBinding;
import com.example.medca.models.booked;
import com.example.medca.models.doctorViewAppointments;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AppointmentRequest extends AppCompatActivity {

    private ActivityAppointmentRequestBinding binding;
    private PreferenceManager preferenceManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        binding = ActivityAppointmentRequestBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());


        getAppointments();
        getApprovedAppointments();

        binding.imagebackAppointmentRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), homepage_doctor.class);
                startActivity(intent);
            }
        });
    }



    private void getAppointments() {{

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_BOOKINGS)
                .whereEqualTo(Constants.KEY_BOOKINGS_DOCTOR, preferenceManager.getString(Constants.KEY_NAME))
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null) {
                        List<doctorViewAppointments> appointments = new ArrayList<>();

                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }

                            doctorViewAppointments appointments1 = new doctorViewAppointments();
                            appointments1.setAppointDate(queryDocumentSnapshot.getString(Constants.KEY_SCHEDULE_DATE));
                            appointments1.setAppointTime(queryDocumentSnapshot.getString(Constants.KEY_SCHEDULE_TIME));
                            appointments1.setDoctor(queryDocumentSnapshot.getString(Constants.KEY_DOCTOR_NAME));
                            appointments1.setPatientName(queryDocumentSnapshot.getString(Constants.KEY_PATIENT_NAME));
                            appointments1.setDocumentID(queryDocumentSnapshot.getString("documentId"));
                            appointments1.setAppointReason(queryDocumentSnapshot.getString("appointmentreason"));

                            appointments.add(appointments1);


                        }
                        if(appointments.size() > 0) {

                            DoctorAppointmentAdapter doctorAppointmentAdapter = new DoctorAppointmentAdapter(this,AppointmentRequest.this,appointments);
                            binding.bookRequest.setAdapter(doctorAppointmentAdapter);
                            binding.bookRequest.setVisibility(View.VISIBLE);
                        }else {

                        }
                    }
                });
    }
    }


    private void getApprovedAppointments() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        List<doctorViewAppointments> appointment = new ArrayList<>();
        database.collection("appointments")
                .whereEqualTo(Constants.KEY_BOOKINGS_DOCTOR, preferenceManager.getString(Constants.KEY_NAME))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            return;
                        }
                        for(DocumentChange doc : value.getDocumentChanges()){
                            switch(doc.getType()){
                                case ADDED:

                                    doctorViewAppointments appointments1 = new doctorViewAppointments();
                                    appointments1.setAppointDate(doc.getDocument().getString(Constants.KEY_SCHEDULE_DATE));
                                    appointments1.setAppointTime(doc.getDocument().getString(Constants.KEY_SCHEDULE_TIME));
                                    appointments1.setDoctor(doc.getDocument().getString(Constants.KEY_DOCTOR_NAME));
                                    appointments1.setPatientName(doc.getDocument().getString(Constants.KEY_PATIENT_NAME));
                                    appointments1.setDocumentID(doc.getDocument().getString("documentId"));
                                    appointments1.setAppointReason(doc.getDocument().getString("appointmentreason"));

                                    appointment.add(appointments1);
                                    break;
                            }
                        }
                        if(appointment.size() > 0) {
                            DoctorAppointmentAdapter doctorAppointmentAdapter = new DoctorAppointmentAdapter(AppointmentRequest.this,AppointmentRequest.this,appointment);
                            binding.bookApprove.setAdapter(doctorAppointmentAdapter);
                            binding.bookApprove.setVisibility(View.VISIBLE);
                        }else {
                            //showToast("No Booked");

                        }
                    }
                });
    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}