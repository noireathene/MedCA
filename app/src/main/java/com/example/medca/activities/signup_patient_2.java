package com.example.medca.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.medca.R;
import com.example.medca.databinding.SignupDoctor2Binding;
import com.example.medca.databinding.SignupPatient2Binding;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class signup_patient_2 extends AppCompatActivity {


    RadioButton select;
    ImageButton imageback;
    private SignupPatient2Binding binding;
    private PreferenceManager preferenceManager;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = SignupPatient2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageback = findViewById(R.id.imageBack1);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), signup_patient.class);
                startActivity(intent);
            }
        });


        //retrieve bundle extras from previous activity
        Bundle extras = getIntent().getExtras();
        String user = extras.getString("username");
        String pass = extras.getString("password");


        //RADIO GROUP
        binding.gender.clearCheck();


       // cont = (ImageButton) findViewById(R.id.btn_continue4);

        binding.btnContinue4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //obtain data from edit text
                String firstname = binding.etFname.getText().toString();
                String lastname = binding.etLname.getText().toString();
                String birth = binding.etDate.getText().toString();

                //obtain data from radio group
                int selectedGenderId = binding.gender.getCheckedRadioButtonId();
                select = findViewById(selectedGenderId);
                String textGender = select.getText().toString();

                if (TextUtils.isEmpty(firstname)) {
                    Toast.makeText(signup_patient_2.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
                    binding.etFname.setError("First name is required");
                    binding.etFname.requestFocus();
                } else if (TextUtils.isEmpty(lastname)) {
                    Toast.makeText(signup_patient_2.this, "Please enter your last name", Toast.LENGTH_SHORT).show();
                    binding.etLname.setError("Last name is required");
                    binding.etLname.requestFocus();
                } else if (TextUtils.isEmpty(birth)) {
                    Toast.makeText(signup_patient_2.this, "Please enter your birthdate", Toast.LENGTH_SHORT).show();
                    binding.etDate.setError("Birthdate is required");
                    binding.etDate.requestFocus();
                } else if (binding.gender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(signup_patient_2.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                  binding.radioF.setError("Gender is required");
                    binding.radioF.requestFocus();

                } else {
                    registerUser(firstname, lastname, birth, textGender, user, pass);
                    addUser( textGender, user, pass);
                    addPatient(textGender);
                }
            }

        });

    }
    //putting data to cloud store
    private void addUser(String gender, String email, String pass){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();
        String name = binding.etFname.getText().toString() +" "+ binding.etLname.getText().toString();
        data.put(Constants.KEY_NAME, binding.etFname.getText().toString());
        data.put(Constants.KEY_NAME, name);
        data.put(Constants.KEY_BIRTHDAY, binding.etDate.getText().toString());
        data.put(Constants.KEY_GENDER, gender);
        data.put(Constants.KEY_EMAIL, email);
        data.put(Constants.KEY_PASSWORD, pass);
        data.put(Constants.KEY_USER_TYPE, "patient");
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

        private void registerUser(String fname, String lname, String bday, String gender, String username, String password){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(signup_patient_2.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(signup_patient_2.this, "User registered successfully", Toast.LENGTH_LONG).show();
                        FirebaseUser firebaseUser = auth.getCurrentUser();

                        //send verification email
                        firebaseUser.sendEmailVerification();

                        //open login page after successful register
                        Intent intent = new Intent(signup_patient_2.this, loginpage_doctor.class);

                        //prevent user from returning to previous activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();


                    }
                }
            });
        }

    private void addPatient(String gender){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();
        String name = binding.etFname.getText().toString() +" "+ binding.etLname.getText().toString();
        data.put(Constants.KEY_NAME, binding.etFname.getText().toString());
        data.put(Constants.KEY_NAME, name);
        data.put(Constants.KEY_BIRTHDAY, binding.etDate.getText().toString());
        data.put(Constants.KEY_GENDER, gender);
        data.put(Constants.KEY_USER_TYPE, "patient");
        database.collection(Constants.KEY_COLLECTION_PATIENTS)
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
    }
