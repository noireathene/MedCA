package com.example.medca.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import com.example.medca.R;
import com.example.medca.alarmreminder.data.Model;
import com.example.medca.alarmreminder.reminder.myAdapter;
import java.util.ArrayList;

public class AlarmList extends AppCompatActivity implements myAdapter.OnMedListener{

        ImageButton imageback;
        RecyclerView mRecyclerview;
        ArrayList<Model> dataholder = new ArrayList<Model>();                                               //Array list to add reminders and display in recyclerview
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

            mRecyclerview = (RecyclerView) findViewById(R.id.notification_view);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


            Bundle bundle = getIntent().getExtras();
            String name = bundle.getString("name");

            Cursor cursor = new com.example.medca.alarmreminder.data.dbManager(getApplicationContext()).readPatientMed(name);                  //Cursor To Load data From the database
            while (cursor.moveToNext()) {
                com.example.medca.alarmreminder.data.Model model = new com.example.medca.alarmreminder.data.Model(cursor.getString(1), cursor.getString(2), cursor.getString(3));
                dataholder.add(model);
            }

            adapter = new myAdapter(dataholder, this);
            mRecyclerview.setAdapter(adapter);                                                          //Binds the adapter with recyclerview
        }

        @Override
        public void onMedClick(int position) {
            Intent intent = new Intent(getApplicationContext(), prescriptionpage.class);
            startActivity(intent);
        }

    }
