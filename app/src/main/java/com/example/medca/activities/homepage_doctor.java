package com.example.medca.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.medca.R;
import com.example.medca.databinding.HomepageDoctorBinding;
import com.example.medca.listeners.UserListener;
import com.example.medca.models.User;
import com.example.medca.models.doctorViewAppointments;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class homepage_doctor extends AppCompatActivity {
    ImageButton search;
    private HomepageDoctorBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private PreferenceManager preferenceManager;
    DrawerLayout drawerLayout;
    NavigationView naview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        binding = HomepageDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        search = findViewById(R.id.searchbar);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), searchpage.class);
                startActivity(intent);
            }
        });

        preferenceManager = new PreferenceManager(getApplicationContext());
//get Name
        binding.tvDoctorname.setText(preferenceManager.getString(Constants.KEY_NAME));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        binding.searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homepage_doctor.this, searchpage.class);
                startActivity(intent);
                finish();
            }
        });

        binding.giveprescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homepage_doctor.this, doctor_patientview.class);
                startActivity(intent);
                finish();
            }
        });

        //appointment history
        binding.btnAppointmentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homepage_doctor.this, appointmenthistory.class);
                startActivity(intent);
                finish();
            }
        });

        //appointment request

        binding.btnAppointmentRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AppointmentRequest.class);
                startActivity(intent);

            }
        });


        //navigation bar functions
        binding.btnMessage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainMessage.class);
                startActivity(intent);
            }
        });

        //navigation bar functions
        binding.btnNotification1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DoctorNotificationList.class);
                startActivity(intent);
            }
        });




        //navigation slide menu select item

        binding.naview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.signOut:
                        signOut();
                        startActivity(new Intent(getApplicationContext(), loginpage_doctor.class));
                        overridePendingTransition(0,0);

                        return true;
                }
                return false;
            }
        });

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Log.d("fcm_token", "FCM fetching failed",task.getException());
                    return;
                }
                String token = task.getResult();

                Log.d("fcm_token", token);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("MedCA_Users").document(preferenceManager.getString(Constants.KEY_USER_ID))
                        .update("fcmToken", token).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d("fcm_token", "token updated");
                                }
                            }
                        });
            }
        });
    }
    //logout
    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.Clear();
                    startActivity(new Intent(getApplicationContext(), loginpage_doctor.class));
                    finish();
                })
        .addOnFailureListener(e -> Toast.makeText(homepage_doctor.this, "Unable to sign out", Toast.LENGTH_SHORT).show());
    }


}

