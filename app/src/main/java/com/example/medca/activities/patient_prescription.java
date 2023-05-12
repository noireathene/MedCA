package com.example.medca.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.medca.R;
import com.example.medca.databinding.PrescriptionpageBinding;
import com.example.medca.utilities.PreferenceManager;

public class patient_prescription extends AppCompatActivity {

    private PrescriptionpageBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = PrescriptionpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get data from patient view

        Bundle bundle = getIntent().getExtras();

        String name = bundle.getString("name");
        String bday = bundle.getString("bday");
        String gender = bundle.getString("gender");

        binding.etPresfname.setText(name);
        binding.etPresbirth.setText(bday);
        binding.etPresgender.setText(gender);

        binding.enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //data passing
                Intent intent = new Intent(getApplicationContext(), doctor_add_prescription.class);
                String name, bday, gender, medpres, intake, sched;
                name = binding.etPresfname.getText().toString();
                bday = binding.etPresbirth.getText().toString();
                gender = binding.etPresgender.getText().toString();
                medpres = binding.etMedicineprescribed.getText().toString();
                intake = binding.etIntake.getText().toString();
                sched = binding.etSchedule.getText().toString();
                intent.putExtra("name", name);
                intent.putExtra("bday", bday);
                intent.putExtra("gender", gender);
                intent.putExtra("medpres", medpres);
                intent.putExtra("intake", intake);
                intent.putExtra("sched", sched);
                startActivity(intent);
                //  addPrescription();

            }
        });

    }

    /**  private void addPrescription(){
     FirebaseFirestore database = FirebaseFirestore.getInstance();
     HashMap<String, Object> data = new HashMap<>();
     data.put(Constants.KEY_FIRST_NAME, binding.etPresfname.getText().toString());
     data.put(Constants.KEY_LAST_NAME, binding.etPreslname.getText().toString());
     data.put("age", binding.tvPresage.getText().toString());
     data.put(Constants.KEY_BIRTHDAY, binding.etPresbirth.getText().toString());
     data.put(Constants.KEY_GENDER, binding.etPresgender.getText().toString());
     data.put("medicine_prescription", binding.etMedicineprescribed.getText().toString());
     data.put("medicine_intake", binding.etIntake.getText().toString());
     data.put("intake_schedule", binding.etSchedule.getText().toString());
     database.collection("prescription")
     .add(data)
     .addOnSuccessListener(documentReference -> {
     Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
     Intent intent = new Intent(getApplicationContext(), homepage_doctor.class);
     startActivity(intent);
     })
     .addOnFailureListener(exception -> {
     Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
     });

     }**/
}