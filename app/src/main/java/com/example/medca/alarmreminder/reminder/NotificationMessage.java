package com.example.medca.alarmreminder.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medca.R;
import com.example.medca.activities.homepage_doctor;
import com.example.medca.adapters.homepageAdapter;
import com.example.medca.alarmreminder.MainReminder;
import com.example.medca.databinding.ActivityNotificationMessageBinding;
import com.example.medca.databinding.ActivityNotificationpageBinding;
import com.example.medca.databinding.DoctorPatientdetailsBinding;
import com.example.medca.databinding.PrescriptionpageBinding;
import com.example.medca.models.User;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationMessage extends AppCompatActivity {

    private static final String TAG = "success";
    TextView textView;
    private ActivityNotificationMessageBinding binding;
    private PreferenceManager preferenceManager;
    String patient, doctor, time, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        binding = ActivityNotificationMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



               // setContentView(R.layout.activity_notification_message);
               // textView = findViewById(R.id.tv_message);
                Bundle bundle = getIntent().getExtras();                                                    //call the data which is passed by another intent
                binding.tvMessage.setText(bundle.getString("message"));


        patient = bundle.getString("patient");
        doctor = bundle.getString("doctor");
        time = bundle.getString("time");
        date = bundle.getString("date");

        binding.dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveId(patient, date, time);
                Intent intent = new Intent(getApplicationContext(), MainReminder.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void updateMedication(String id){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection("medications").document(id);
        documentReference
                .update("status", "taken")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        });
    }

    private void retrieveId(String patient, String date, String time){


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("medications")
                .whereEqualTo(Constants.KEY_PATIENT_NAME, patient)
                .whereEqualTo("date", date)
                .whereEqualTo("time", time)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = patient;
                    if(task.isSuccessful() && task.getResult() != null){
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            String id = queryDocumentSnapshot.getId();
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            updateMedication(id);
                            Log.d("TAG_11","ID: "+id);
                        }
                    }
                    else{

                    }

                });
    }
}