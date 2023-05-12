package com.example.medca.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.medca.adapters.UsersAdapter;
import com.example.medca.adapters.patientUsersAdapter;
import com.example.medca.databinding.ActivityDisplayUserBinding;
import com.example.medca.databinding.ActivityPatientDisplayUserBinding;
import com.example.medca.listeners.UserListener;
import com.example.medca.listeners.patientUserListener;
import com.example.medca.models.User;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class patient_display_user extends BaseActivity implements patientUserListener, UserListener {

    private ActivityPatientDisplayUserBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        binding = ActivityPatientDisplayUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }

    private void setListeners() {binding.imageback.setOnClickListener(v -> onBackPressed()); }

    private void getUsers(){
        isLoading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo("userType", "patient")
                .get()
                .addOnCompleteListener(task -> {
                    isLoading(false);
                    String currentUserId = preferenceManager.getString((Constants.KEY_USER_ID));
                    if(task.isSuccessful() && task.getResult() != null){
                        List<User> users = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);
                        }
                        if(users.size()>0){
                            patientUsersAdapter usersAdapter = new patientUsersAdapter(users, this);
                            binding.usersRecycleView.setAdapter(usersAdapter);
                            binding.usersRecycleView.setVisibility(View.VISIBLE);
                        }else{
                            showErrorMessage();
                        }
                    }
                    else{
                        showErrorMessage();
                    }

                });
    }

    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void isLoading (Boolean isLoading){
        if (isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), chat_page.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}