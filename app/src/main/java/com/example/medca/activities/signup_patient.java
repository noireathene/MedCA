package com.example.medca.activities;

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
import android.widget.Toast;

import com.example.medca.R;

public class signup_patient extends AppCompatActivity {
    ImageButton imageback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        setContentView(R.layout.signup_patient);

        Button cont;
        EditText username, password, repassword;

        username = (EditText) findViewById(R.id.edittext_email);
        password = (EditText) findViewById(R.id.edittext_password);
        repassword = (EditText) findViewById(R.id.repassword);
        cont = (Button) findViewById(R.id.btn_continue3);
        imageback = findViewById(R.id.imageBack);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), signuppage.class);
                startActivity(intent);
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if(TextUtils.isEmpty(user)){
                    Toast.makeText(signup_patient.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    username.setError("Email is required");
                    username.requestFocus();
                }else if(TextUtils.isEmpty(pass)){
                    Toast.makeText(signup_patient.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    password.setError("Password is required");
                    password.requestFocus();
                }else if(pass.length() < 6){
                    Toast.makeText(signup_patient.this, "Password should be 6 characters or longer", Toast.LENGTH_SHORT).show();
                    password.setError("Password too weak");
                    password.requestFocus();
                }else if(!pass.equals(repass)){
                    Toast.makeText(signup_patient.this, "Password should be identical", Toast.LENGTH_SHORT).show();
                    repassword.setError("Password confirmation required");
                    repassword.requestFocus();

                    //clear password field
                    password.clearComposingText();
                    repassword.clearComposingText();
                }else {
                    Intent intent = new Intent(signup_patient.this, signup_patient_2.class);
                    Bundle extras = new Bundle();
                    extras.putString("username",user);
                    extras.putString("password",pass);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}