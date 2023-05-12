package com.example.medca.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.medca.R;
import com.example.medca.adapters.BookingAdapter;
import com.example.medca.databinding.ActivityBookingsListBinding;
import com.example.medca.models.booked;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingsActivity extends AppCompatActivity {


    private ActivityBookingsListBinding binding;
    private PreferenceManager preferenceManager;
    private ImageView cancelSchedule;
    private Boolean isReceiverAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        binding = ActivityBookingsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();
        getApproved();
        getCounselling();

        binding.imagebackBookingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingsActivity.this, homepage_patient.class);
                startActivity(intent);
            }
        });

    }

    private void getCounselling() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_BOOKINGS)
                .whereEqualTo(Constants.KEY_BOOKINGS_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null) {
                        List<booked> bookeds = new ArrayList<>();
                        booked booked = new booked();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            booked.bookdate = queryDocumentSnapshot.getString(Constants.KEY_SCHEDULE_DATE);
                            booked.booktime = queryDocumentSnapshot.getString(Constants.KEY_SCHEDULE_TIME);
                            booked.bookstatus = queryDocumentSnapshot.getString(Constants.KEY_BOOKINGS_STATUS);
                            booked.bookreason = queryDocumentSnapshot.getString("appointmentreason");
                            bookeds.add(booked);
                        }
                        if(bookeds.size() > 0) {
                            BookingAdapter bookingAdapter = new BookingAdapter(bookeds);
                            binding.bookCounselling.setAdapter(bookingAdapter);
                            binding.bookCounselling.setVisibility(View.VISIBLE);
                        }else {

                        }
                    }else {

                    }
                });
    }

   private void getApproved() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_APPROVED_BOOKINGS)
                .whereEqualTo(Constants.KEY_BOOKINGS_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                //  .whereEqualTo(Constants.KEY_BOOKINGS_TypeOfBooking, Constants.KEY_BOOKINGS_COUNSELLING)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null) {
                        List<booked> bookeds = new ArrayList<>();
                        booked booked = new booked();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            booked.bookdate = queryDocumentSnapshot.getString(Constants.KEY_SCHEDULE_DATE);
                            booked.booktime = queryDocumentSnapshot.getString(Constants.KEY_SCHEDULE_TIME);
                            booked.bookstatus = queryDocumentSnapshot.getString(Constants.KEY_BOOKINGS_STATUS);
                            bookeds.add(booked);
                        }
                        if(bookeds.size() > 0) {
                            BookingAdapter bookingAdapter = new BookingAdapter(bookeds);
                            binding.bookCounselling.setAdapter(bookingAdapter);
                            binding.bookCounselling.setVisibility(View.VISIBLE);
                        }else {

                        }
                    }else {

                    }
                });
    }


    private void loadUserDetails() {
        String fname = preferenceManager.getString(Constants.KEY_NAME);
        String fullName = fname; /* + " " + mname + " " + lname; */
        binding.textName.setText(fullName);

    }

    private void loading(Boolean isLoading) {
        if(isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}