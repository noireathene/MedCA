package com.example.medca.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.medca.R;
import com.example.medca.activities.homepage_doctor;
import com.example.medca.alarmreminder.reminder.AlarmBrodcast;
import com.example.medca.utilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class doctor_add_prescription extends AppCompatActivity {
    Button mSubmitbtn, mDatebtn, mTimebtn ;
    Spinner mTitledit;
    String timeTonotify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        setContentView(R.layout.activity_doctor_add_prescription);

        mTitledit = (Spinner) findViewById(R.id.editTitle);
        mDatebtn = (Button) findViewById(R.id.btnDate);                                             //assigned all the material reference to get and set data
        mTimebtn = (Button) findViewById(R.id.btnTime);
        mSubmitbtn = (Button) findViewById(R.id.btnSbumit);


        //get extras from prescription page
        Bundle bundle = getIntent().getExtras();
        String name, bday, gender,medpres, intake, sched;
        name = bundle.getString("name");
        bday = bundle.getString("bday");
        gender = bundle.getString("gender");
        medpres = bundle.getString("medpres");
        intake = bundle.getString("intake");
        sched = bundle.getString("sched");

        mTimebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();                                                                       //when we click on the choose time button it calls the select time method
            }
        });

        mDatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }                                        //when we click on the choose date button it calls the select date method
        });


        //SUBMIT BUTTON FOR ALARM
        mSubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitledit.getSelectedItem().toString().trim();                               //access the data form the input field
                String date = mDatebtn.getText().toString().trim();                                 //access the date form the choose date button
                String time = mTimebtn.getText().toString().trim();                                 //access the time form the choose time button

                if (title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter text", Toast.LENGTH_SHORT).show();   //shows the toast if input field is empty
                } else {
                    if (time.equals("time") || date.equals("date")) {                                               //shows toast if date and time are not selected
                        Toast.makeText(getApplicationContext(), "Please select date and time", Toast.LENGTH_SHORT).show();
                    } else {
                       processinsert(name,title, date, time);
                            addPrescription(name, bday, gender, title, medpres, intake, sched);
                    }
                }


            }
        });
    }


    private void processinsert(String name, String title, String date, String time) {
        String result = new com.example.medca.alarmreminder.data.dbManager(this).addreminder(name, title, date, time);                  //inserts the title,date,time into sql lite database
        //setAlarm(title, date, time);                                                                //calls the set alarm method to set alarm
      //  mTitledit.getSelectedItem();
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }

    private void selectTime() {                                                                     //this method performs the time picker task
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;                                                        //temp variable to store the time to set alarm
                mTimebtn.setText(FormatTime(i, i1));                                                //sets the button text as selected time
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    private void selectDate() {                                                                     //this method performs the date picker task
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mDatebtn.setText(day + "-" + (month + 1) + "-" + year);                             //sets the selected date as test for button
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public String FormatTime(int hour, int minute) {                                                //this method converts the time into 12hr farmat and assigns am or pm

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }


    //FIREBASE METHOD FOR SAVING
    private void addPrescription(String name, String bday, String gender, String medicine,String medpres, String intake, String sched){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();
        data.put(Constants.KEY_NAME, name);
        data.put(Constants.KEY_BIRTHDAY, bday);
        data.put(Constants.KEY_GENDER, gender);
        data.put("medicine", medicine);
        data.put("medpres", medpres);
        data.put("intake", intake);
        data.put("sched", sched);
        data.put("status", "alarm not set");
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

    }
}