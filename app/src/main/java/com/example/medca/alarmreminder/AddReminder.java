package com.example.medca.alarmreminder;

import static com.google.firebase.messaging.Constants.TAG;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medca.R;
import com.example.medca.activities.homepage_doctor;
import com.example.medca.alarmreminder.reminder.AlarmBrodcast;
import com.example.medca.databinding.ActivityAddReminderBinding;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


//this class is to add the take the reminders from the user and inserts into database
public class AddReminder extends AppCompatActivity {

    Button mSubmitbtn, mDatebtn, mTimebtn;
    TextView mTitledit, mDoctor;
    String timeTonotify;

    private ActivityAddReminderBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar

        binding = ActivityAddReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

          mTitledit = (TextView) findViewById(R.id.editTitle);
          mSubmitbtn = (Button) findViewById(R.id.btnSbumit);
          mDatebtn = (Button) findViewById(R.id.btnDate);
          mTimebtn = (Button) findViewById(R.id.btnTime);
          mDoctor = (TextView) findViewById(R.id.tv_doctor);





        preferenceManager = new PreferenceManager(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        String name, medicine, doctor, diagnosis;
        name = bundle.getString("name");
        medicine = bundle.getString("medicine");
        doctor = bundle.getString("doctor");
        diagnosis = bundle.getString("diagnosis");

        binding.tvPatient.setText(name);
        binding.editTitle.setText(medicine);
        binding.tvDoctor.setText(doctor);




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

        mSubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                          //access the data form the input field
                String date = mDatebtn.getText().toString().trim();                                 //access the date form the choose date button
                String time = mTimebtn.getText().toString().trim();                                 //access the time form the choose time button
                String title = mTitledit.getText().toString();
                String doctor = mDoctor.getText().toString();


                    if (time.equals("time") || date.equals("date")) {                                               //shows toast if date and time are not selected
                        Toast.makeText(getApplicationContext(), "Please select date and time", Toast.LENGTH_SHORT).show();
                    } else {
                        processinsert(name, title, date, time, doctor);
                        addMedications(name, title, date, time, doctor, diagnosis);
                        retrieveId(name, doctor, title);

                    }



            }
        });
    }


    private void processinsert(String name, String title, String date, String time, String doctor) {
        String result = new com.example.medca.alarmreminder.data.dbManager(this).addreminder(name, title, date, time);                  //inserts the title,date,time into sql lite database
        setAlarm(title, date, time, name, doctor);                                                                //calls the set alarm method to set alarm
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }
    private void addMedications(String name, String title, String time, String date, String doctor, String diagnosis) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();
        data.put(Constants.KEY_PATIENT_NAME, name);
        data.put("medicine", title);
        data.put("time", time);
        data.put("date", date);
        data.put("diagnosis", diagnosis);
        data.put("status", "not taken");
        data.put(Constants.KEY_DOCTOR_NAME, doctor);
        database.collection("medications")
                .add(data)
                .addOnSuccessListener(documentReference -> {

                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                });

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


    private void setAlarm(String text, String date, String time, String name, String doctor) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);                   //assigining alaram manager object to set alaram

        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("event", text);                                                       //sending data to alarm class to create channel and notification
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        intent.putExtra("patient", name);
        intent.putExtra("doctor", doctor);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(getApplicationContext(), "Alarm", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intentBack = new Intent(getApplicationContext(), com.example.medca.activities.homepage_patient.class);                //this intent will be called once the setting alaram is completes
        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentBack);                                                                  //navigates from adding reminder activity ot mainactivity

    }

    private void updateMedication(String id){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection("prescription").document(id);
        documentReference
                .update("status", "alarm has set")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String TAG = "ok";
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                });
    }
    private void retrieveId(String patient, String doctor, String med){

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("prescription")
                .whereEqualTo(Constants.KEY_PATIENT_NAME, patient)
                .whereEqualTo(Constants.KEY_DOCTOR_NAME, doctor)
                .whereEqualTo("medicine_prescription", med)
                .whereEqualTo("status", "alarm not set")
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = patient;
                    if(task.isSuccessful() && task.getResult() != null){
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            String id = queryDocumentSnapshot.getId();
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            updateMedication(id);
                        }
                    }
                    else{

                    }

                });
    }
}
