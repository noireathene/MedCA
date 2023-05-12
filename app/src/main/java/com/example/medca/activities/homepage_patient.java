package com.example.medca.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.medca.PrescriptionList;
import com.example.medca.R;
import com.example.medca.adapters.UsersAdapter;
import com.example.medca.adapters.homepageAdapter;
import com.example.medca.adapters.medAdapter;
import com.example.medca.alarmreminder.Alarm_Detail;
import com.example.medca.alarmreminder.MainReminder;
import com.example.medca.alarmreminder.data.Model;
import com.example.medca.alarmreminder.reminder.myAdapter;
import com.example.medca.databinding.HomepagePatientBinding;
import com.example.medca.listeners.UserListener;
import com.example.medca.models.User;

import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class homepage_patient extends AppCompatActivity {

    private HomepagePatientBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private PreferenceManager preferenceManager;
    DrawerLayout drawerLayout;
    RecyclerView mRecyclerview;
    ArrayList<com.example.medca.alarmreminder.data.Model> dataholder = new ArrayList<Model>();
    myAdapter adapter;
    NavigationView naview1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        binding = HomepagePatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());
//get Name
        binding.tvpatientname1.setText(preferenceManager.getString(Constants.KEY_NAME));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        String name = binding.tvpatientname1.getText().toString();


        //recycleview


     //   retrieve();

        //navigation bar functions
        binding.btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), patient_main_message.class);
                startActivity(intent);
            }
        });

        //add medication button

        binding.btnMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainReminder.class);
                intent.putExtra("name", binding.tvpatientname1.getText().toString());
                startActivity(intent);

            }
        });

        //navigation bar functions
        binding.btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PatientNotificationList.class);
                startActivity(intent);
            }
        });


        //add search for doctors button

        binding.btnSearchDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), appointment.class);
                startActivity(intent);
            }
        });

        //search bar

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homepage_patient.this, searchpage.class);
                startActivity(intent);
                finish();
            }
        });

        //view appointment

        binding.btnAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookingsActivity.class);
                startActivity(intent);

            }
        });

        //view my prescription

        binding.btnPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrescriptionList.class);
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
         .addOnFailureListener(e -> Toast.makeText(homepage_patient.this, "Unable to sign out", Toast.LENGTH_SHORT).show());
    }


}