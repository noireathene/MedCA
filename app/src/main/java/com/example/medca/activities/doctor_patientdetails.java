package com.example.medca.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medca.R;
import com.example.medca.adapters.BookingAdapter;
import com.example.medca.adapters.UsersAdapter;
import com.example.medca.adapters.medAdapter;
import com.example.medca.adapters.medicationAdapter;
import com.example.medca.alarmreminder.data.Model;
import com.example.medca.alarmreminder.reminder.myAdapter;
import com.example.medca.databinding.ActivityMainMessageBinding;
import com.example.medca.databinding.DoctorPatientdetailsBinding;
import com.example.medca.listeners.UserListener;
import com.example.medca.models.User;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class doctor_patientdetails extends AppCompatActivity implements UserListener {
    ImageButton imageback;


    DoctorPatientdetailsBinding binding;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        binding = DoctorPatientdetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        preferenceManager = new PreferenceManager(getApplicationContext());


        imageback = findViewById(R.id.btn_back2);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), doctor_patientview.class);
                startActivity(intent);
            }
        });


        //get data from recycler view from previous activity
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        binding.tvPatientname.setText(name);

    getPrescriptionDetails(name);


        binding.btnchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), display_user.class);
                startActivity(intent);
            }
        });

        binding.btnpres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), prescriptionpage.class);
                intent.putExtra("name", binding.tvPatientname.getText());

                startActivity(intent);
            }
        });


        //retrieve info of patient

    }

    private void getPrescriptionDetails(String name){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("medications")

                .whereEqualTo(Constants.KEY_BOOKINGS_DOCTOR, preferenceManager.getString(Constants.KEY_NAME))
                .whereEqualTo(Constants.KEY_PATIENT_NAME, name)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = preferenceManager.getString((Constants.KEY_USER_ID));
                    if(task.isSuccessful() && task.getResult() != null){
                        List<User> users = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }

                            User user2 = new User();
                            user2.name = queryDocumentSnapshot.getString(Constants.KEY_BOOKINGS_DOCTOR);
                            user2.medicine = queryDocumentSnapshot.getString("medicine");
                            user2.schedule = queryDocumentSnapshot.getString("date");
                            user2.time = queryDocumentSnapshot.getString("time");
                            user2.diagnosis = queryDocumentSnapshot.getString("diagnosis");
                            user2.status = queryDocumentSnapshot.getString("status");
                            users.add(user2);
                        }
                        if(users.size()>0){
                            medicationAdapter medicationAdapter = new medicationAdapter(users,this);
                            binding.recyclerView1.setAdapter(medicationAdapter);
                            binding.recyclerView1.setVisibility(View.VISIBLE);

                        }else{

                        }
                    }
                    else{

                    }

                });
    }



    @Override
    public void onUserClicked(User user) {

    }
}