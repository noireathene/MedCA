package com.example.medca.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.medca.R;
import com.example.medca.adapters.DoctorNotificationAdapter;
import com.example.medca.databinding.ActivityDoctorNotificationListBinding;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import FCM.NotificationModel;

public class DoctorNotificationList extends AppCompatActivity {

    ActivityDoctorNotificationListBinding binding;
    private FirebaseFirestore db;
    private ArrayList<NotificationModel> arrListNotification;
    private DoctorNotificationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        binding = ActivityDoctorNotificationListBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);


        initVariables();
    }
    private void initVariables(){
        db = FirebaseFirestore.getInstance();

        arrListNotification = new ArrayList<>();

        initRecyclerView();
    }
    private void initRecyclerView(){
        binding.notificationRecyclerView1.setLayoutManager(new LinearLayoutManager(DoctorNotificationList.this));
        adapter = new DoctorNotificationAdapter(arrListNotification, DoctorNotificationList.this, DoctorNotificationList.this);
        binding.notificationRecyclerView1.setAdapter(adapter);

        adapter.setOnItemClickListener(new DoctorNotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getApplicationContext(), AppointmentRequest.class);
                startActivity(intent);
            }
        });

        getNotifications();
    }
    private void getNotifications(){
        PreferenceManager preferenceManager = new PreferenceManager(DoctorNotificationList.this);
        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);

        db.collection("Notifications").whereEqualTo("userId", currentUserId)
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            switch (dc.getType()){
                                case ADDED:
                                    NotificationModel notificationModel = dc.getDocument().toObject(NotificationModel.class);
                                    arrListNotification.add(notificationModel);
                                    break;
                            }
                        }
                        if(!arrListNotification.isEmpty()){
                            binding.notificationRecyclerView1.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}