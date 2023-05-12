package com.example.medca.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medca.activities.AppointmentRequest;
import com.example.medca.databinding.AppointmentRequestItemBinding;
import com.example.medca.models.doctorViewAppointments;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import FCM.FCMAppointmentStatusNotification;
import FCM.NotificationModel;

public class DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentAdapter.AppointmentViewHolder>{

    private final List<doctorViewAppointments> appointments;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private Activity activity;
    public DoctorAppointmentAdapter(AppointmentRequest appointmentRequest, Activity activity,List<doctorViewAppointments> appointments) {
        this.appointments = appointments;
        this.activity = activity;
    }

    private Context context;

    private PreferenceManager preferenceManager;

    @NonNull
    @Override
    public DoctorAppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AppointmentRequestItemBinding appointmentRequestItemBinding = AppointmentRequestItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        context = parent.getContext();
        preferenceManager = new PreferenceManager(context);
        return new AppointmentViewHolder(appointmentRequestItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAppointmentAdapter.AppointmentViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        doctorViewAppointments mAppointments  = appointments.get(position);

        holder.binding.bookedDate.setText(appointments.get(position).appointDate);
        holder.binding.bookedTime.setText(appointments.get(position).appointTime);
        holder.binding.bookedReason.setText(appointments.get(position).appointReason);
        holder.binding.txtName.setText(appointments.get(position).patientName);
        holder.binding.txtReason.setText(appointments.get(position).docName);


    if(mAppointments.getDocumentID() != null){
        database.collection(Constants.KEY_COLLECTION_APPROVED_BOOKINGS).document(mAppointments.getDocumentID())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        holder.binding.linearLayoutControls.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }



        holder.binding.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                        .setTitle("Cancel Schedule")
                        .setMessage("Are you sure want to Cancel?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseFirestore database = FirebaseFirestore.getInstance();

                                database.collection("bookingCollection").document(mAppointments.documentID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            String userId = task.getResult().getString("senderId");
                                            String body = "Your appointment with "+task.getResult().getString("doctorName")+" is cancelled";
                                            database.collection("MedCA_Users").document(task.getResult().getString("senderId"))
                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if(task.isSuccessful()){

                                                                FCMAppointmentStatusNotification sendFcm = new FCMAppointmentStatusNotification(
                                                                        task.getResult().getString("fcmToken"),
                                                                        "Appointment Cancelled",body,

                                                                        context, activity);

                                                                sendFcm.sendNotification();

                                                                Date currentTime = Calendar.getInstance().getTime();
                                                                DocumentReference docRef = database.collection("Notifications").document();

                                                                NotificationModel notificationModel = new NotificationModel();
                                                                notificationModel.setDocId(docRef.getId());
                                                                notificationModel.setDataFrom("FcmAppointmentStatusNotification");
                                                                notificationModel.setTitle("Appointment Cancelled");
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
                                            database.collection(Constants.KEY_COLLECTION_BOOKINGS)
                                                    // .document(preferenceManager.getString(Constants.KEY_BOOKING_ID))
                                                    .document(mAppointments.documentID)
                                                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CANCEL_BOOKINGS).document();

                                                            appointments.remove(position);
                                                            notifyItemRemoved(position);
                                                            Toast.makeText(context, "Schedule is removed", Toast.LENGTH_SHORT).show();
                                                            HashMap<String, Object> cancelAppointment = new HashMap<>();
                                                            cancelAppointment.put(Constants.KEY_BOOKINGS_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                                                            cancelAppointment.put(Constants.KEY_BOOKINGS_TypeOfBooking, Constants.KEY_BOOKINGS_TypeOfBooking);
                                                            cancelAppointment.put(Constants.KEY_BOOKINGS_LNAME, preferenceManager.getString(Constants.KEY_BOOKINGS_LNAME));
                                                            cancelAppointment.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
                                                            cancelAppointment.put(Constants.KEY_MIDDLE_NAME, preferenceManager.getString(Constants.KEY_MIDDLE_NAME));
                                                            cancelAppointment.put(Constants.KEY_BOOKINGS_ADDRESS, preferenceManager.getString(Constants.KEY_BOOKINGS_ADDRESS));
                                                            cancelAppointment.put(Constants.KEY_EMAIL, preferenceManager.getString(Constants.KEY_EMAIL));
                                                            cancelAppointment.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));
                                                            cancelAppointment.put(Constants.KEY_BOOKINGS_STATUS, "Cancelled");
                                                            cancelAppointment.put("notificationType ", "Bookings");
                                                            cancelAppointment.put("read", false);
                                                            cancelAppointment.put(Constants.KEY_CREATED_AT, new Date());
                                                            cancelAppointment.put("documentId", documentReference.getId());
                                                            //database.collection(Constants.KEY_COLLECTION_CANCEL_BOOKINGS).add(cancelAppointment);

                                                            documentReference.set(cancelAppointment);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();
            }
        });

        holder.binding.btnapproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                        .setTitle("Approve Schedule?")
                        .setMessage("Are you sure you want to approve?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                 database.collection("bookingCollection").document(mAppointments.documentID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        String sched =  mAppointments.getAppointDate() + " "+ mAppointments.getAppointTime();
                                        String userID = task.getResult().getString("senderId");
                                        String body = "Your appointment with "+task.getResult().getString("doctorName")+" is approved on "+ sched;
                                        database.collection("MedCA_Users").document(task.getResult().getString("senderId"))
                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if(task.isSuccessful()){

                                                            FCMAppointmentStatusNotification sendFcm = new FCMAppointmentStatusNotification(
                                                                    task.getResult().getString("fcmToken"),
                                                                    "Appointment Approved",body,
                                                                    context, activity);

                                                            sendFcm.sendNotification();

                                                            Date currentTime = Calendar.getInstance().getTime();
                                                            DocumentReference docRef = database.collection("Notifications").document();

                                                            NotificationModel notificationModel = new NotificationModel();
                                                            notificationModel.setDocId(docRef.getId());
                                                            notificationModel.setDataFrom("FcmAppointmentNotification");
                                                            notificationModel.setTitle("Appointment Approved");
                                                            notificationModel.setBody(body);
                                                            notificationModel.setUserId(userID);
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

                                        database.collection(Constants.KEY_COLLECTION_BOOKINGS)
                                                .document(mAppointments.documentID)
                                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        FirebaseFirestore database = FirebaseFirestore.getInstance();



                                                        DocumentReference docRef = database.collection("patient_list").document();
                                                        String docId1 = docRef.getId();
                                                        Query notEqualPatient = docRef.getParent().whereEqualTo(Constants.KEY_PATIENT_NAME, mAppointments.getPatientName());
                                                        notEqualPatient.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if(task.isSuccessful()) {
                                                                    QuerySnapshot qSnap = task.getResult();
                                                                    if (qSnap.isEmpty()) {
                                                                        HashMap<String, Object> patients = new HashMap<>();
                                                                        patients.put(Constants.KEY_PATIENT_NAME, mAppointments.getPatientName());
                                                                        patients.put(Constants.KEY_DOCTOR_NAME, mAppointments.getDoctor());
                                                                        patients.put("documentId", docId1);
                                                                        docRef.set(patients);
                                                                        Log.d("Query Data", "Data Inserted");
                                                                    } else {
                                                                        Log.d("Query Data", "Data is not valid");


                                                                    }

                                                                }
                                                            }
                                                        });

                                                        DocumentReference documentReference =  database.collection(Constants.KEY_COLLECTION_APPROVED_BOOKINGS).document();
                                                        String docId = documentReference.getId();
                                                        appointments.remove(position);
                                                        notifyItemRemoved(position);
                                                        Toast.makeText(context, "Schedule is approved", Toast.LENGTH_SHORT).show();
                                                        HashMap<String, Object> approvedAppointment = new HashMap<>();
                                                        approvedAppointment.put(Constants.KEY_BOOKINGS_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                                                        approvedAppointment.put(Constants.KEY_PATIENT_NAME, mAppointments.getPatientName());
                                                        approvedAppointment.put(Constants.KEY_BOOKINGS_DOCTOR, mAppointments.getDoctor());
                                                        approvedAppointment.put(Constants.KEY_BOOKINGS_ADDRESS, preferenceManager.getString(Constants.KEY_BOOKINGS_ADDRESS));
                                                        approvedAppointment.put(Constants.KEY_EMAIL, preferenceManager.getString(Constants.KEY_EMAIL));
                                                        approvedAppointment.put(Constants.KEY_BOOKINGS_STATUS, "Approved");
                                                        approvedAppointment.put("notificationType ", "Bookings");
                                                        approvedAppointment.put("read", false);
                                                        approvedAppointment.put(Constants.KEY_CREATED_AT, new Date());
                                                        approvedAppointment.put("documentId",docId);
                                                        documentReference.set(approvedAppointment);


                                                        HashMap<String, Object> appointment = new HashMap<>();
                                                        appointment.put(Constants.KEY_SCHEDULE_DATE, mAppointments.getAppointDate());
                                                        appointment.put(Constants.KEY_SCHEDULE_TIME, mAppointments.getAppointTime());
                                                        appointment.put(Constants.KEY_BOOKINGS_REASON, mAppointments.getAppointReason());
                                                        appointment.put(Constants.KEY_BOOKINGS_DOCTOR,mAppointments.getDoctor());
                                                        appointment.put(Constants.KEY_PATIENT_NAME, mAppointments.getPatientName());
                                                        appointment.put(Constants.KEY_BOOKINGS_SENDER_ID, mAppointments.getPatientID());
                                                        appointment.put("documentId", docId);

                                                        database.collection("appointments").document(docId).set(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Log.d("TEST_TAG", "OK");

                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("TEST_TAG", e.getMessage());
                                                            }
                                                        });
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {

        AppointmentRequestItemBinding binding;

        AppointmentViewHolder(AppointmentRequestItemBinding itemAppointmentBinding) {
            super(itemAppointmentBinding.getRoot());
            binding = itemAppointmentBinding;
        }

        void setAppointmentData(doctorViewAppointments appointments) {
            binding.bookedDate.setText(appointments.appointDate);
            binding.bookedTime.setText(appointments.appointTime);
            binding.bookedReason.setText(appointments.appointReason);
            binding.txtReason.setText(appointments.docName);
            binding.txtName.setText(appointments.patientName);

        }
    }

    //FOR ADDING USER TO USER LIST
    private void addPatient(String patientname, String doctor) {
        database.collection("patient_list")
                .whereEqualTo(Constants.KEY_PATIENT_NAME, patientname)
                .whereEqualTo(Constants.KEY_DOCTOR_NAME, doctor)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = patientname;
                    if(task.isSuccessful() && task.getResult() == null){
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            HashMap<String, Object> data = new HashMap<>();
                            data.put(Constants.KEY_PATIENT_NAME, patientname);
                            data.put(Constants.KEY_DOCTOR_NAME, doctor);
                            database.collection("patient_list")
                                    .add(data)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d("TEST_TAG", "OK");
                                    })
                                    .addOnFailureListener(exception -> {
                                        Log.d("TEST_TAG", "fail");
                                    });
                        }
                    }
                    else{


                    }

                });

    }


}