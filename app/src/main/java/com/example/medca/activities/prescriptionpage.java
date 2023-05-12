package com.example.medca.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medca.R;
import com.example.medca.alarmreminder.AddReminder;
import com.example.medca.databinding.PrescriptionpageBinding;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import FCM.FCMAppointmentNotification;
import FCM.FCMPrescriptionNotification;
import FCM.NotificationModel;

public class prescriptionpage extends AppCompatActivity {
    ImageButton imageback;
    private PrescriptionpageBinding binding;
    private PreferenceManager preferenceManager;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = PrescriptionpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        userId = intent.getStringExtra("user.id");
        imageback = findViewById(R.id.btn_back);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), homepage_doctor.class);
                startActivity(intent);
            }
        });

        //get data from patient view

        Bundle bundle = getIntent().getExtras();

        String name = bundle.getString("name");


        binding.searchMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), searchpage.class);
                startActivity(intent);
            }
        });

        binding.etPresfname.setText(name);

        binding.enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                retrieveId(name, "patient");
            
            }
        });

    }

    private void addPrescription(String userId){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();
        data.put(Constants.KEY_PATIENT_NAME, binding.etPresfname.getText().toString());
        data.put(Constants.KEY_BIRTHDAY, binding.etPresbirth.getText().toString());
        data.put(Constants.KEY_GENDER, binding.etPresgender.getText().toString());
        data.put("diagnosis", binding.etDiagnosed.getText().toString());
        data.put("medicine_prescription", binding.etMedicineprescribed.getText().toString());
        data.put("medicine_intake", binding.etIntake.getText().toString());
        data.put("intake_schedule", binding.etSchedule.getText().toString());
        data.put(Constants.KEY_BOOKINGS_DOCTOR, preferenceManager.getString(Constants.KEY_NAME));
        data.put("status", "alarm not set");
        database.collection("prescription")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), homepage_doctor.class);
                    startActivity(intent);


                    database.collection("MedCA_Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){

                              //  String sched = binding.textScheduleDateSet.getText().toString() + " " +binding.textScheduleTimeSet.getText().toString();
                                FCMPrescriptionNotification sendFCM = new FCMPrescriptionNotification(task.getResult().getString("fcmToken"),
                                        "New prescription has been given", "A new prescription has been given by your doctor", prescriptionpage.this, prescriptionpage.this);

                                sendFCM.sendNotification();

                                Date currentTime = Calendar.getInstance().getTime();
                                DocumentReference docRef = database.collection("Notifications").document();

                                NotificationModel notificationModel = new NotificationModel();
                                notificationModel.setDocId(docRef.getId());
                                notificationModel.setDataFrom("FcmPrescriptionNotification");
                                notificationModel.setTitle("New prescription has been given");
                                notificationModel.setBody("A new prescription has been given by your doctor");
                                notificationModel.setUserId(userId);
                                notificationModel.setDate(currentTime);

                                docRef.set(notificationModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                        }
                                    }
                                });
                            }
                        }
                    });


                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
    private void retrieveId(String patient, String type){

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_NAME, patient)
                .whereEqualTo("userType", type)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = patient;
                    if(task.isSuccessful() && task.getResult() != null){
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            String id = queryDocumentSnapshot.getId();
                            String TAG = "ok";
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            addPrescription(id);
                        }
                    }
                    else{

                    }

                });
    }
}