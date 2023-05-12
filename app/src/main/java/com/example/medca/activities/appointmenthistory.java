package com.example.medca.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.medca.R;
import com.example.medca.databinding.ActivityAppointmenthistoryBinding;
import com.example.medca.databinding.ActivityPatientviewItemBinding;
import com.example.medca.databinding.DoctorPatientviewBinding;
import com.example.medca.listeners.UserListener;
import com.example.medca.models.User;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class appointmenthistory extends AppCompatActivity implements UserListener {

    private ActivityAppointmenthistoryBinding binding;
    private ActivityPatientviewItemBinding itembinding;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        setContentView(R.layout.doctor_patientview);
        binding = ActivityAppointmenthistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        getUsers();
    }

    private void getUsers() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        List<User> users = new ArrayList<>(); // BAGO
        myPatientViewAdapter adapter = new myPatientViewAdapter(users, this); // BAGO

        database.collection(Constants.KEY_COLLECTION_APPOINTMENT)
                .whereEqualTo(Constants.KEY_BOOKINGS_DOCTOR, preferenceManager.getString(Constants.KEY_NAME))
                .get()
                .addOnCompleteListener(task -> {

                    String currentUserId = preferenceManager.getString((Constants.KEY_USER_ID));
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_PATIENT_NAME);
                            user.id = queryDocumentSnapshot.getId();
                            user.schedule = queryDocumentSnapshot.getString("schedule_date");
                            user.time = queryDocumentSnapshot.getString("schedule_time");
                            user.reason = queryDocumentSnapshot.getString("appointmentreason");
                            users.add(user);
                            Log.d("TEST_TAG", user.name);
                        }
                        if (users.size() > 0) {
                            binding.recyclerView.setAdapter(adapter);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                        } else {
                        }
                    } else {
                    }

                });
    }

    @Override
    public void onUserClicked(User user) {

    }
}