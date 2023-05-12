package com.example.medca.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.medca.databinding.SigninchoiceBinding;

public class signinchoice extends AppCompatActivity {

    private SigninchoiceBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        binding = SigninchoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       /** ImageButton patient;
        ImageButton doctor;

        patient = (ImageButton) findViewById(R.id.btn_patient);
        doctor = (ImageButton) findViewById(R.id.btn_doctor);
**/
        binding.btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), loginpage_doctor.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), loginpage_doctor.class);
                startActivity(intent);
                finish();
            }
        });

    }
}