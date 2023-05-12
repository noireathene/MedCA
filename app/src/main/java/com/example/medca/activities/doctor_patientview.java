package com.example.medca.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.medca.R;
import com.example.medca.adapters.patientUsersAdapter;
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

public class doctor_patientview extends AppCompatActivity implements UserListener{



    private DoctorPatientviewBinding binding;
    private ActivityPatientviewItemBinding itembinding;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        setContentView(R.layout.doctor_patientview);
        binding = DoctorPatientviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         preferenceManager = new PreferenceManager(getApplicationContext());

        getUsers();
        binding.tvDocname.setText(preferenceManager.getString(Constants.KEY_NAME));


        binding.imageBackPatientView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), homepage_doctor.class);
                startActivity(intent);
            }
        });

    }

    private void getUsers(){
        isLoading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        List<User> users = new ArrayList<>(); // BAGO
        patientUsersAdapter adapter = new patientUsersAdapter(users, this); // BAGO

        database.collection("patient_list")
                .whereEqualTo(Constants.KEY_BOOKINGS_DOCTOR, preferenceManager.getString(Constants.KEY_NAME))
                .get()
                .addOnCompleteListener(task -> {
                    isLoading(false);
                    String currentUserId = preferenceManager.getString((Constants.KEY_USER_ID));
                    if(task.isSuccessful() && task.getResult() != null){
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_PATIENT_NAME);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);
                            Log.d("TEST_TAG", user.name);
                        }
                        if(users.size()>0){
                            binding.recyclerView.setAdapter(adapter);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                        }else{
                        }
                    }
                    else{
                    }

                });
    }

    private void isLoading (Boolean isLoading){
        if (isLoading){
         } else {
           }
    }



      @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), doctor_patientdetails.class);
        String patientname = user.name;
        intent.putExtra(Constants.KEY_USER, user);
        intent.putExtra("name", patientname);
        startActivity(intent);
        finish();
    }
}