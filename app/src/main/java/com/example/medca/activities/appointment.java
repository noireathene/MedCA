package com.example.medca.activities;

import static com.google.firebase.messaging.Constants.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.medca.R;
import com.example.medca.databinding.ActivityAppointmentBinding;
import com.example.medca.listeners.ITimeSlotLoadListener;
import com.example.medca.models.booked;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import FCM.FCMAppointmentNotification;
import FCM.NotificationModel;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;

public class appointment extends AppCompatActivity {

    private ActivityAppointmentBinding binding;
    private PreferenceManager preferenceManager;
    private TextView onDisplayDate, onDisplayTime;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    int hour, minute;
    DocumentReference docRef;
    String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String docName = intent.getStringExtra("name");
        String medfield = intent.getStringExtra("medfield");
        userId = intent.getStringExtra("user.id");
        binding.textDoctor.setText(docName);
        binding.textDoctorMedField.setText(medfield);
        preferenceManager = new PreferenceManager(getApplicationContext());
        buttonTime();
        setListeners();
    }

    private void setListeners() {
        setTime();
        setDate();
        setDoctor();
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.buttonSubmit.setOnClickListener(v -> doneSchedule());
    }

    private void setDoctor(){
        binding.setDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchDoctor.class);
                startActivity(intent);
            }
        });
    }

    private void setDate() {
        onDisplayDate = (TextView) findViewById(R.id.textScheduleDateSet);

        binding.setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(appointment.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        onDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                cal.add(Calendar.MONTH, 2);
                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                onDisplayDate.setText(date);
                binding.textScheduleDateSet.setVisibility(View.VISIBLE);
            }
        };
    }

    private void setTime() {
        onDisplayTime = (TextView) findViewById(R.id.textScheduleTimeSet);

        binding.setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(appointment.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                hour = i;
                                minute = i1;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,hour, minute);
                                onDisplayTime.setText(DateFormat.format("hh:mm aa", calendar));
                                binding.textScheduleTimeSet.setVisibility(View.VISIBLE);
                            }
                        },
                        12, 0, false);
                dialog.updateTime(hour, minute);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    private void doneSchedule(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        loading(true);;
        if(isValidSignUpDetails()) {
           database.collection(Constants.KEY_COLLECTION_BOOKINGS)
                    .whereEqualTo(Constants.KEY_BOOKINGS_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                    .whereEqualTo(Constants.KEY_BOOKINGS_TypeOfBooking, Constants.KEY_BOOKINGS_ASSESSMENT)
                    .get()
                    .addOnCompleteListener(task -> {
                        loading(false);
                        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                        if(task.isSuccessful() && task.getResult() != null) {
                            List<booked> bookeds = new ArrayList<>();
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                if(currentUserId.equals(queryDocumentSnapshot.getId())) {
                                    continue;
                                }
                                booked booked = new booked();
                                booked.bookdate = queryDocumentSnapshot.getString(Constants.KEY_SCHEDULE_DATE);
                                booked.booktime = queryDocumentSnapshot.getString(Constants.KEY_SCHEDULE_TIME);
                                booked.bookreason = queryDocumentSnapshot.getString(Constants.KEY_BOOKINGS_REASON);
                                booked.bookstatus = queryDocumentSnapshot.getString(Constants.KEY_BOOKINGS_STATUS);
                                bookeds.add(booked);
                            }
                            if(bookeds.size() < 1) {
                                docRef =   database.collection(Constants.KEY_COLLECTION_BOOKINGS).document();

                                Toast.makeText(appointment.this, "Appointment has been Booked. Please wait for the Approval.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(appointment.this, BookingsActivity.class));
                                HashMap<String, Object> bookAssessment = new HashMap<>();
                                bookAssessment.put(Constants.KEY_BOOKINGS_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                                bookAssessment.put(Constants.KEY_BOOKINGS_TypeOfBooking, Constants.KEY_BOOKINGS_ASSESSMENT);
                                bookAssessment.put(Constants.KEY_PATIENT_NAME, preferenceManager.getString(Constants.KEY_NAME));
                                bookAssessment.put(Constants.KEY_BOOKINGS_CONTACT_NUMBER, preferenceManager.getString(Constants.KEY_BOOKINGS_CONTACT_NUMBER));
                                bookAssessment.put(Constants.KEY_BOOKINGS_ADDRESS, preferenceManager.getString(Constants.KEY_BOOKINGS_ADDRESS));
                                bookAssessment.put(Constants.KEY_EMAIL, preferenceManager.getString(Constants.KEY_EMAIL));
                                bookAssessment.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));
                                bookAssessment.put(Constants.KEY_BOOKINGS_STATUS, binding.textStatus.getText().toString());
                                bookAssessment.put(Constants.KEY_SCHEDULE_DATE, binding.textScheduleDateSet.getText().toString());
                                bookAssessment.put(Constants.KEY_SCHEDULE_TIME, binding.textScheduleTimeSet.getText().toString());
                                bookAssessment.put(Constants.KEY_BOOKINGS_DOCTOR, binding.textDoctor.getText().toString());
                                bookAssessment.put(Constants.KEY_BOOKINGS_DOCTOR_MEDFIELD, binding.textDoctorMedField.getText().toString());
                                bookAssessment.put(Constants.KEY_BOOKINGS_REASON, binding.txtReason.getText().toString());
                                bookAssessment.put(Constants.KEY_CREATED_AT, new Date());
                                bookAssessment.put("documentId", docRef.getId());



                                docRef.set(bookAssessment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            preferenceManager.putString(Constants.KEY_ASSESSMENT_ID, docRef.getId());

                                            database.collection("MedCA_Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                 if(task.isSuccessful()){

                                                     String sched = binding.textScheduleDateSet.getText().toString() + " " +binding.textScheduleTimeSet.getText().toString();
                                                     String body = preferenceManager.getString(Constants.KEY_NAME)+" has set an appointment at "+sched;
                                                     FCMAppointmentNotification sendFCM = new FCMAppointmentNotification(task.getResult().getString("fcmToken"),
                                                             "New appointment", body, appointment.this, appointment.this);

                                                     sendFCM.sendNotification();

                                                     Date currentTime = Calendar.getInstance().getTime();
                                                     DocumentReference docRef = database.collection("Notifications").document();

                                                     NotificationModel notificationModel = new NotificationModel();
                                                     notificationModel.setDocId(docRef.getId());
                                                     notificationModel.setDataFrom("FcmAppointmentNotification");
                                                     notificationModel.setTitle("New appointment");
                                                     notificationModel.setBody(body);
                                                     notificationModel.setUserId(userId);
                                                     notificationModel.setDate(currentTime);

                                                     docRef.set(notificationModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<Void> task) {
                                                             if(task.isSuccessful()){

                                                             }
                                                         }
                                                     });
                                                 }
                                                }
                                            });

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loading(false);
                                        showToast(e.getMessage());
                                    }
                                });


                            }else {
                                showToast("You Already Booked this Appointment");
                            }
                        }else {
                            //showToast("No Booked");
                        }
                    });
        }else {
            loading(false);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading (Boolean isLoading) {
        if(isLoading) {
            binding.buttonSubmit.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSubmit.setVisibility(View.VISIBLE);
        }
    }

    private Boolean isValidSignUpDetails() {
        if (binding.textScheduleTimeSet.getText().toString().trim().isEmpty()) {
            showToast("Error! Select Time Schedule");
            return false;
        } else if (binding.textScheduleDateSet.getText().toString().trim().isEmpty()) {
            showToast("Error! Select Date Schedule");
            return false;
        } else if (binding.txtReason.getText().toString().trim().isEmpty()) {
            showToast("Enter Your Reason of Booking.");
            return false;
        }
        else
            return true;
    }

    private void buttonTime() {
        binding.button9am.setOnClickListener(v -> {
            binding.textScheduleTimeSet.setText("9:00 AM");
            binding.textScheduleTimeSet.setVisibility(View.VISIBLE);
        });
        binding.button10am.setOnClickListener(v -> {
            binding.textScheduleTimeSet.setText("10:00 AM");
            binding.textScheduleTimeSet.setVisibility(View.VISIBLE);
        });
        binding.button1pm.setOnClickListener(v -> {
            binding.textScheduleTimeSet.setText("1:00 PM");
            binding.textScheduleTimeSet.setVisibility(View.VISIBLE);
        });
        binding.button3pm.setOnClickListener(v -> {
            binding.textScheduleTimeSet.setText("3:00 PM");
            binding.textScheduleTimeSet.setVisibility(View.VISIBLE);
        });
    }
}

