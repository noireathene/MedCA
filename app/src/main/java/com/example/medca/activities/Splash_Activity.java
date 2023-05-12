package com.example.medca.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medca.R;

public class Splash_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        setContentView(R.layout.activity_splash);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
        public void run() {
            startActivity(new Intent(getApplicationContext(), loginpage_doctor.class));             //after 500 milliseconds this block calls the mainActivity
            finish();
        }
    }, secondsDelayed * 500);
}
}