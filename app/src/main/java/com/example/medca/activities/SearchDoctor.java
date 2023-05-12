package com.example.medca.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.medca.R;
import com.example.medca.adapters.UsersAdapter;
import com.example.medca.adapters.UsersAdapterDoctor;
import com.example.medca.databinding.ActivityLoginpageDoctorBinding;
import com.example.medca.databinding.ActivitySearchDoctorBinding;
import com.example.medca.listeners.UserListenerDoctors;
import com.example.medca.models.User;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchDoctor extends AppCompatActivity implements UserListenerDoctors {

    private ActivitySearchDoctorBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        binding = ActivitySearchDoctorBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());

        getUsers();



    }

    private void getUsers(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo("userType", "doctor")
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = preferenceManager.getString((Constants.KEY_USER_ID));
                    if(task.isSuccessful() && task.getResult() != null){
                        List<User> users = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_MEDFIELD);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);

                            Log.d("TEST_TAG", "Name: "+user.name +"\n id: "+user.id );
                        }
                        if(users.size()>0){
                            UsersAdapterDoctor usersAdapter = new UsersAdapterDoctor(users, this);
                            binding.doctorView.setAdapter(usersAdapter);
                            binding.doctorView.setVisibility(View.VISIBLE);
                        }else{
                          //  showErrorMessage();
                        }
                    }
                    else{
                       // showErrorMessage();
                    }

                });
    }


    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(SearchDoctor.this, appointment.class);
        intent.putExtra("name", user.name);
        intent.putExtra("medfield", user.email);
        intent.putExtra("user.id", user.id);
        startActivity(intent);
    }
}

