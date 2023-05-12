package com.example.medca.alarmreminder;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medca.PrescriptionList;
import com.example.medca.R;
import com.example.medca.activities.Patient_PrescriptionView;
import com.example.medca.adapters.medicationAdapter;
import com.example.medca.alarmreminder.data.Model;
import com.example.medca.alarmreminder.reminder.myAdapter;
import com.example.medca.databinding.ActivityMainReminderBinding;
import com.example.medca.databinding.DoctorPatientdetailsBinding;
import com.example.medca.listeners.UserListener;
import com.example.medca.models.User;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class MainReminder extends AppCompatActivity implements myAdapter.OnMedListener, UserListener {//implements myAdapter.OnMedListener{

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String TAG = "MainReminder";
    FloatingActionButton mCreateRem;
    RecyclerView mRecyclerview;
    ArrayList<com.example.medca.alarmreminder.data.Model> dataholder = new ArrayList<com.example.medca.alarmreminder.data.Model>();                                               //Array list to add reminders and display in recyclerview
    myAdapter adapter;

    FirebaseFirestore database;
    PreferenceManager preferenceManager;

    ActivityMainReminderBinding binding;

    List<User> users;
    medicationAdapter medicationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        setContentView(R.layout.activity_main_reminder);

        binding = ActivityMainReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        preferenceManager = new PreferenceManager(getApplicationContext());

        try {
            Bundle bundle = getIntent().getExtras();
            String name = bundle.getString("name");
            //get intent data from homepatient
            Cursor cursor = new com.example.medca.alarmreminder.data.dbManager(getApplicationContext()).readPatientMed(name);                  //Cursor To Load data From the database
            while (cursor.moveToNext()) {
                com.example.medca.alarmreminder.data.Model model = new com.example.medca.alarmreminder.data.Model(cursor.getString(1), cursor.getString(2), cursor.getString(3));
                dataholder.add(model);
            }
                 //Binds the adapter with recyclerview


        } catch (Exception e) {
            Log.d("TEST_TAG", e.getMessage());
        }

        users = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        medicationAdapter = new medicationAdapter(users,this);
        binding.recyclerView.setAdapter(medicationAdapter);
        getData();

    }
    public void getData(){

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("medications")

                .whereEqualTo(Constants.KEY_PATIENT_NAME, preferenceManager.getString(Constants.KEY_NAME))
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = preferenceManager.getString((Constants.KEY_USER_ID));
                    if(task.isSuccessful() && task.getResult() != null){
                       for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }

                            User user2 = new User();
                             user2.name = queryDocumentSnapshot.getString(Constants.KEY_BOOKINGS_DOCTOR);
                             user2.medicine = queryDocumentSnapshot.getString("medicine");
                            user2.schedule = queryDocumentSnapshot.getString("date");
                            user2.time = queryDocumentSnapshot.getString("time");
                            user2.status = queryDocumentSnapshot.getString("status");
                            user2.diagnosis = queryDocumentSnapshot.getString("diagnosis");
                            users.add(user2);

                            Log.d("TAG_2", user2.name);
                            Log.d("TAG_2","Size: "+users.size());
                        }
                        medicationAdapter.notifyDataSetChanged();
                        if(users.size()>0){
                            binding.recyclerView.setVisibility(View.VISIBLE);

                        }else{

                             }
                    }
                    else{

                       }

                });

    }



    @Override
    public void onBackPressed() {
        finish();                                                                                   //Makes the user to exit form the app
        super.onBackPressed();

    }

    @Override
    public void onMedClick(int position) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onUserClicked(User user) {

    }
}