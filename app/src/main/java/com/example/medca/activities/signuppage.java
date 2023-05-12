package com.example.medca.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.medca.R;

public class signuppage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        setContentView(R.layout.signuppage);

        Button patient;
        Button doctor;

        patient = (Button) findViewById(R.id.btn_patient1);
        doctor = (Button) findViewById(R.id.btn_doctor1);

        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(signuppage.this, signup_patient.class);
                startActivity(intent);
                finish();
            }
        });

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(signuppage.this, signup_doctor.class);
                startActivity(intent);
                finish();
            }
        });
    }
}