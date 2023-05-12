package com.example.medca.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.medca.R;
import com.example.medca.databinding.SignupDoctor2Binding;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

public class signup_doctor_2 extends AppCompatActivity {
    ImageButton imageback;
    Button cont;
    EditText fname, lname, bday;
    RadioGroup rGroup;
    RadioButton select;
    Spinner medfield;
    String textGender, user, pass;
    private SignupDoctor2Binding binding;
    private PreferenceManager preferenceManager;
    private String encodeImage;

    FirebaseStorage storage;
    StorageReference storageReference;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = SignupDoctor2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //retrieve bundle extras from previous activity
        Bundle extras = getIntent().getExtras();
        String user = extras.getString("username");
        String pass = extras.getString("password");


        //RADIO GROUP
        binding.gendergroup.clearCheck();

        //SPINNER CONTENT FOR MEDICAL FIELD
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.medfield, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.medicalfield.setAdapter(adapter);

        imageback = findViewById(R.id.imageBack3);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), signup_doctor.class);
                startActivity(intent);
            }
        });

        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        binding.doctorContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //obtain data from edit text
                String firstname = binding.etFname.getText().toString();
                String lastname = binding.etLname.getText().toString();
                String birth = binding.etDate.getText().toString();

                //obtain data from radio group
                int selectedGenderId = binding.gendergroup.getCheckedRadioButtonId();
                select = findViewById(selectedGenderId);
                textGender = select.getText().toString();
                //obtain data from spinner
                String mfield = binding.medicalfield.getSelectedItem().toString();

                if(TextUtils.isEmpty(firstname)){
                    Toast.makeText(signup_doctor_2.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
                    binding.etFname.setError("First name is required");
                    binding.etFname.requestFocus();
                }else if(TextUtils.isEmpty(lastname)){
                    Toast.makeText(signup_doctor_2.this, "Please enter your last name", Toast.LENGTH_SHORT).show();
                    binding.etLname.setError("Last name is required");
                    binding.etLname.requestFocus();
                }else if(TextUtils.isEmpty(birth)){
                    Toast.makeText(signup_doctor_2.this, "Please enter your birthdate", Toast.LENGTH_SHORT).show();
                    binding.etDate.setError("Birthdate is required");
                    binding.etDate.requestFocus();
                }else if(binding.gendergroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(signup_doctor_2.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                    // binding.setError("Gender is required");
                    //select.requestFocus();
                }else if(binding.medicalfield.getSelectedItemId()== -1){
                    Toast.makeText(signup_doctor_2.this, "Please select your medical field", Toast.LENGTH_SHORT).show();
                    // fname.setError("Medical field is required");
                    //  fname.requestFocus();
                }else{

                    addUser( textGender, mfield, user, pass);
                    registerUser(firstname, lastname, birth, textGender, mfield, user, pass);
                }


            }
        });

    }

    // method for selecting image
    private void SelectImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImage.launch(intent);
    }


    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if (result.getData() !=null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageview.setImageBitmap(bitmap);
                            encodeImage = encodeImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                binding.imageview.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


    //putting data to cloud store
    private void addUser(String gender, String med, String email, String pass){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();
        String name = binding.etFname.getText().toString() +" "+ binding.etLname.getText().toString();
        data.put(Constants.KEY_NAME, binding.etFname.getText().toString());
        data.put(Constants.KEY_NAME, name);
        data.put(Constants.KEY_BIRTHDAY, binding.etDate.getText().toString());
        data.put(Constants.KEY_GENDER, gender);
        data.put(Constants.KEY_MEDFIELD, med);
        data.put(Constants.KEY_EMAIL, email);
        data.put(Constants.KEY_PASSWORD, pass);
        data.put(Constants.KEY_USER_TYPE, "doctor");
        data.put("proofImage", encodeImage);
        data.put("userStatus", "not verified");
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

    //register user with the credentials given
    private void registerUser(String fname, String lname, String bday, String gender, String medfield, String username, String password){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(signup_doctor_2.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(signup_doctor_2.this, "User registered successfully", Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //send verification email
                    firebaseUser.sendEmailVerification();

                    //open login page after successful register
                    Intent intent = new Intent(signup_doctor_2.this, loginpage_doctor.class);

                    //prevent user from returning to previous activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();


                }
            }
        });

    }

}