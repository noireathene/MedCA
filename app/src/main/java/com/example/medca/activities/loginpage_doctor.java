package com.example.medca.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medca.R;
import com.example.medca.databinding.ActivityLoginpageDoctorBinding;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;


public class loginpage_doctor extends AppCompatActivity {

    private ActivityLoginpageDoctorBinding binding;
    private PreferenceManager preferenceManager;

    //NECESSARY FOR GOOGLE AUTHENTICATION
    private static final String TAG = "GoogleActivity";

    private static final int RC_SIGN_IN = 2;

    private GoogleSignInClient mGoogleSignInClient;

    FirebaseAuth mAuth;

    //FIREBASE AUTH LISTENER
    FirebaseAuth.AuthStateListener mAuthlistener;


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthlistener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        binding = ActivityLoginpageDoctorBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());





        //MAUTHLISTENER
        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(loginpage_doctor.this, signinchoice.class));
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.def_web_cli_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //END OF GOOGLE AUTHENTICATION

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        //GOOGLE AUTHENTICATION SIGN IN

        binding.googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //CLICK LISTENERS
        binding.btnLogin0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = binding.edittextEmail.getText().toString();
                String textpassword = binding.edittextPassword.getText().toString();

                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(loginpage_doctor.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    binding.edittextEmail.setError("Email is required");
                    binding.edittextEmail.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(loginpage_doctor.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    binding.edittextEmail.setError("Email address is not valid");
                    binding.edittextEmail.requestFocus();
                } else if(TextUtils.isEmpty(textpassword)){
                    Toast.makeText(loginpage_doctor.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    binding.edittextPassword.setError("Password is required");
                    binding.edittextPassword.requestFocus();
                } else{

                    //loginUser (textEmail, textpassword);
                    login();
                }


    }
    });


        binding.signup3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginpage_doctor.this, signuppage.class);
                startActivity(intent);
                finish();
            }
        });

        binding.forgotpass3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // **END OF CLICK LISTENERS **//
    }

    // Configure Google Sign In

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(loginpage_doctor.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(loginpage_doctor.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }


                    }
                });
    }
    // [END auth_with_google]


    private void loginUser(String txtemail, String txtpassword){
        mAuth.signInWithEmailAndPassword(txtemail, txtpassword).addOnCompleteListener(loginpage_doctor.this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(loginpage_doctor.this, "Login successful", Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(loginpage_doctor.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void login (){
        FirebaseFirestore data = FirebaseFirestore.getInstance();
        data.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.edittextEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD,binding.edittextPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() !=null && task.getResult().getDocuments().size() > 0){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        String userType = documentSnapshot.getString(Constants.KEY_USER_TYPE);


                        if (userType.equals("doctor")) {
                            String userStatus = documentSnapshot.getString("userStatus");
                            if(userStatus.equals("verified")){
                            Intent intent = new Intent(getApplicationContext(), homepage_doctor.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);}
                            else {
                                Toast.makeText(loginpage_doctor.this, "Unable to sign in", Toast.LENGTH_SHORT).show();
                            }
                        }else if (userType.equals("patient")){
                            Intent intent = new Intent(getApplicationContext(), homepage_patient.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                    }else {
                        Toast.makeText(loginpage_doctor.this, "Unable to sign in", Toast.LENGTH_SHORT).show();
                    }
                }
    });

    }
    private void updateUI(FirebaseUser user) {

    }

}

