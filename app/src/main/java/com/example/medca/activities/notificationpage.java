package com.example.medca.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.medca.R;
import com.example.medca.alarmreminder.data.Model;
import com.example.medca.alarmreminder.reminder.myAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class notificationpage extends AppCompatActivity {
    ImageButton imageback;
    RecyclerView mRecyclerview;
    ArrayList<Model> dataholder = new ArrayList<com.example.medca.alarmreminder.data.Model>();                                               //Array list to add reminders and display in recyclerview
    myAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        setContentView(R.layout.activity_notificationpage);

        imageback = findViewById(R.id.imageBack5);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), homepage_patient.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomnav;
        bottomnav = (BottomNavigationView) findViewById(R.id.bottom_navi);
        mRecyclerview = (RecyclerView) findViewById(R.id.notification_view);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        //navigation bar functions
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.mess_navi1:
                        startActivity(new Intent(getApplicationContext(), patient_main_message.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.home_navi1:
                        startActivity(new Intent(getApplicationContext(), homepage_patient.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.notif_navi1:

                        return true;
                }
                return false;
            }

        });


    }
}