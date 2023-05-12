package com.example.medca.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.medca.PrescriptionList;
import com.example.medca.R;
import com.example.medca.activities.chat_page;
import com.example.medca.activities.homepage_doctor;
import com.example.medca.activities.homepage_patient;
import com.example.medca.models.User;
import com.example.medca.utilities.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.Console;
import java.util.Map;
import java.util.Random;

public class MessagingService extends FirebaseMessagingService {
    String dataFrom;
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);


    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        dataFrom = data.get("dataFrom");
        if(dataFrom != null){
           switch (dataFrom){
               case "FcmAppointmentNotification":
                   AppointmentNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
                   break;
               case "FcmAppointmentStatusNotification":
                   AppointmentStatusNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
                   break;
               case "FcmPrescriptionNotification":
                   PrescriptionNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
                   break;
           }
        }

    }
    public void PrescriptionNotification(String body, String title){
        String Channel_Id = "prescription_notification";
        NotificationManager mManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(Channel_Id,
                    "Prescription Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription("Prescription Notification Channel");

            mManager.createNotificationChannel(mChannel);
        }

        Intent intent = new Intent(this, PrescriptionList.class);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), Channel_Id)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentIntent(pendingIntent)
                .build();

        mManager.notify((int) System.currentTimeMillis(), notification);
    }
    public void AppointmentNotification(String body, String title){
        String Channel_Id = "appointment_notification";
        NotificationManager mManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(Channel_Id,
                    "Appointment Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription("Appointment Notification Channel");

            mManager.createNotificationChannel(mChannel);
        }

        Intent intent = new Intent(this, homepage_doctor.class);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), Channel_Id)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentIntent(pendingIntent)
                .build();

        mManager.notify((int) System.currentTimeMillis(), notification);
    }
    public void AppointmentStatusNotification(String body, String title){
        String Channel_Id = "appointment_notification";
        NotificationManager mManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(Channel_Id,
                    "Appointment Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription("Appointment Notification Channel");

            mManager.createNotificationChannel(mChannel);
        }

        Intent intent = new Intent(this, homepage_patient.class);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), Channel_Id)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentIntent(pendingIntent)
                .build();

        mManager.notify((int) System.currentTimeMillis(), notification);
    }
}
