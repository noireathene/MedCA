package com.example.medca.utilities;

import com.google.android.gms.common.internal.ConnectionTelemetryConfiguration;

import java.util.HashMap;

public class Constants {

    public static final String KEY_COLLECTION_USERS = "MedCA_Users";
    public static final String KEY_NAME = "name";
    public static final String KEY_PATIENT_NAME = "patientName";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_MEDFIELD = "medical_field";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_TYPE = "userType";
    public static final String KEY_PREFERENCE_NAME = "chatAppPrefence";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverID";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static final String KEY_COLLECTION_PATIENTS = "Patient_Info";
    public static final String KEY_PATIENT_BIRTHDAY = "birthday";
    public static final String KEY_PATIENT_GENDER = "gender";
    public static final String KEY_MEDICINE = "medicine name";
    public static final String KEY_COLLECTION_APPOINTMENT = "appointments";
    public static final String KEY_APPOINTMENT_ID = "appointmentId";
    public static final String KEY_APPOINTMENT_TYPE = "appointmentType";
    public static final String KEY_APPOINTMENT_ASSESSMENT = "assessment";
    public static final String KEY_SCHEDULE_DATE = "schedule_date";
    public static final String KEY_SCHEDULE_TIME = "schedule_time";
    public static final String KEY_APPOINTMENT_STATUS = "appoint_status";
    public static final String KEY_APPOINTMENT_NAME = "appointClient";
    public static final String KEY_APPOINTMENT_EMAIL = "emailad";
    public static final String KEY_BOOKINGS_REASON = "appointmentreason";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_ASSESSMENT_ID = "assessmentId";
    public static final String KEY_DOCTOR_NAME = "doctorName";
    public static final String KEY_BOOKINGS_SENDER_ID = "senderId";
    public static final String KEY_BOOKINGS_TypeOfBooking = "typeOfBooking";
    public static final String KEY_MIDDLE_NAME = "middleName";
    public static final String KEY_BOOKINGS_CONTACT_NUMBER = "contactNumber";
    public static final String KEY_BOOKINGS_ADDRESS = "address";
    public static final String KEY_BOOKINGS_STATUS = "bookingStatus";
    public static final String KEY_COLLECTION_BOOKINGS = "bookingCollection";
    public static final String KEY_BOOKINGS_ASSESSMENT = "bookingAssessment";
    public static final String KEY_BOOKINGS_LNAME = "lastName";
    public static final String KEY_BOOKINGS_AGE = "age";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_BOOKING_ID = "bookingID";
    public static final String KEY_COLLECTION_CANCEL_BOOKINGS = "bookingCollectionCancel";
    public static final String KEY_COLLECTION_APPROVED_BOOKINGS = "bookingCollectionApprove";
    public static final String KEY_BOOKINGS_DOCTOR = "doctorName";
    public static final String KEY_BOOKINGS_DOCTOR_MEDFIELD = "doctorMedField";










    public static HashMap<String, String> remoteMsgHeaders= null;
    public static HashMap<String, String> getRemoteMsgHeaders() {
        if(remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(REMOTE_MSG_AUTHORIZATION, "AAAAFskYIOw:APA91bEQSJSUynpdQFcAGyYz2eGPQKyD-ZRL2GM_VKeayQc-bfaX0IVHTb-vuvjBK-qnXjWx09fqN1iTtSOJKlieHKuUX-449p36IDL211bTttKEqCAlC54BkpAPKecFtdWvw7PLHOiN");

            remoteMsgHeaders.put(REMOTE_MSG_CONTENT_TYPE,
                    "application/json");

        }
        return remoteMsgHeaders;
    }

    public static String convertTimeslotToString(int slot){
        switch (slot)
        {
            case 0:
                return "9:00 AM";
            case 1:
                return "10:00 AM";
            case 2:
                return "1:00 PM";
            case 3:
                return "3:00 PM";
            case 4:
                return "5:00 PM";
            default:
                return "closed";
        }
    }
}
