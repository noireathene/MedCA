package com.example.medca.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medca.R;

public class Patient_PrescriptionView extends AppCompatActivity {
    ImageButton imageback;
    TextView fname, lname, age, gender, birth, medpresc, medintake, schedule;
    String mfname, mlname, mage, mgender, mbirth, medpres, intake, sched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        setContentView(R.layout.activity_patient_prescription_view);

       // imageback = findViewById(R.id.btn_back1);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), homepage_patient.class);
                startActivity(intent);
            }
        });

mfname = getIntent().getStringExtra("FirstName");
mlname = getIntent().getStringExtra("LastName");
mage =getIntent().getStringExtra("Age");
mgender = getIntent().getStringExtra("Gender");
mbirth = getIntent().getStringExtra("BirthDate");
medpres = getIntent().getStringExtra("MedPres");
intake = getIntent().getStringExtra("Intake");
sched = getIntent().getStringExtra("Schedule");
fname = (TextView) findViewById(R.id.tv_presfname);
lname = (TextView) findViewById(R.id.tv_preslname);
age = (TextView) findViewById(R.id.tv_presage);
gender = (TextView) findViewById(R.id.tv_presgender);
birth = (TextView) findViewById(R.id.tv_presbirth);
medpresc = (TextView) findViewById(R.id.tv_medicineprescribed);
medintake =(TextView)  findViewById(R.id.tv_intake);
schedule = (TextView) findViewById(R.id.tv_schedule);
fname.setText(mfname);
lname.setText(mlname);
age.setText(mage);
gender.setText(mgender);
birth.setText(mbirth);
medpresc.setText(medpres);
medintake.setText(intake);
schedule.setText(sched);

    }
}