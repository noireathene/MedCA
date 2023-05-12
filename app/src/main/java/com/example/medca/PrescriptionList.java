package com.example.medca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.medca.activities.Patient_PrescriptionView;
import com.example.medca.activities.doctor_patientdetails;
import com.example.medca.activities.myPatientViewAdapter;
import com.example.medca.adapters.ListPrescriptionAdapter;
import com.example.medca.alarmreminder.AddReminder;
import com.example.medca.alarmreminder.MainReminder;
import com.example.medca.alarmreminder.data.Model;
import com.example.medca.alarmreminder.reminder.myAdapter;
import com.example.medca.databinding.ActivityPrescriptionListBinding;
import com.example.medca.databinding.PrescriptionItemBinding;
import com.example.medca.listeners.UserListener;
import com.example.medca.models.User;
import com.example.medca.models.doctorViewAppointments;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionList extends AppCompatActivity implements UserListener {

    private PreferenceManager preferenceManager;
    private ActivityPrescriptionListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        binding = ActivityPrescriptionListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        getUsers();


        binding.imagebackPrescriptionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainReminder.class);
                startActivity(intent);
            }
        });
    }

    private void getUsers(){

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("prescription")
                .whereEqualTo(Constants.KEY_PATIENT_NAME, preferenceManager.getString(Constants.KEY_NAME))
                .get()
                .addOnCompleteListener(task -> {

                    String currentUserId = preferenceManager.getString((Constants.KEY_USER_ID));
                    if(task.isSuccessful() && task.getResult() != null){
                        List<User> users = new ArrayList<>();
                        //User user1 = new User();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
;

                            User user1 = new User();
                            user1.medicine = queryDocumentSnapshot.getString("medicine_prescription");
                            user1.intake = queryDocumentSnapshot.getString("medicine_intake");
                            user1.schedule = queryDocumentSnapshot.getString("intake_schedule");
                            user1.name = queryDocumentSnapshot.getString(Constants.KEY_BOOKINGS_DOCTOR);
                            user1.status = queryDocumentSnapshot.getString("status");
                            user1.diagnosis = queryDocumentSnapshot.getString("diagnosis");
                            users.add(user1);
                        }
                        if(users.size()>0){
                            ListPrescriptionAdapter listPrescriptionAdapter = new ListPrescriptionAdapter(users, this::onUserClicked);
                            binding.recyclerView2.setAdapter(listPrescriptionAdapter);
                            binding.recyclerView2.setVisibility(View.VISIBLE);
                        }else{

                        }
                    }
                    else{

                    }

                });
    }


    @Override
    public void onUserClicked(User user) {

  Intent intent = new Intent(getApplicationContext(), AddReminder.class);
  intent.putExtra("name", preferenceManager.getString(Constants.KEY_NAME));
      intent.putExtra("medicine", user.medicine);
      intent.putExtra("doctor", user.name);
      intent.putExtra("diagnosis", user.diagnosis);
      startActivity(intent);

    }
}





