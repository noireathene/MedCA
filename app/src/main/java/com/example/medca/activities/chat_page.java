package com.example.medca.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.medca.R;
import com.example.medca.adapters.ChatAdapter;
import com.example.medca.databinding.ActivityChatPageBinding;
import com.example.medca.models.ChatMessage;
import com.example.medca.models.User;
import com.example.medca.network.ApiClient;
import com.example.medca.network.ApiService;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class chat_page extends BaseActivity {

    private ActivityChatPageBinding binding;
    private User receiverUSer;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private String conversionId = null;
    private Boolean isReceiverAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        super.onCreate(savedInstanceState);
        binding = ActivityChatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
        loadReceiverDetails();
        init();
        listenMessages();
    }

    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiverUSer.id);
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        if(conversionId != null){
            updateConversion(binding.inputMessage.getText().toString());
        } else{
            HashMap<String, Object> conversion = new HashMap<>();
            conversion.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            conversion.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
            conversion.put(Constants.KEY_RECEIVER_ID, receiverUSer.id);
            conversion.put(Constants.KEY_RECEIVER_NAME, receiverUSer.name);
            conversion.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
            conversion.put(Constants.KEY_TIMESTAMP, new Date());
            addConversion(conversion);
        }
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        if(!isReceiverAvailable){
            try {
                JSONArray tokens = new JSONArray();
                tokens.put(receiverUSer.token);

                JSONObject data = new JSONObject();
                data.put(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                data.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
                data.put(Constants.KEY_FCM_TOKEN, preferenceManager.getString(Constants.KEY_FCM_TOKEN));
                data.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());

                JSONObject body = new JSONObject();
                body.put(Constants.REMOTE_MSG_DATA, data);
                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                sendNotification(body.toString());
            }catch (Exception exception){
                showToast(exception.getMessage());
            }
        }
        binding.inputMessage.setText(null);
    }

    private void listenMessages(){

        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUSer.id)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverUSer.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void sendNotification(String messageBody) {
        ApiClient.getClient().create(ApiService.class).sendMessage(Constants.getRemoteMsgHeaders(),
                messageBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                if(response.isSuccessful()){
                    try {
                        if(response.body() != null) {
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("results");
                            if(responseJson.getInt("failure") == 1){
                                JSONObject error = (JSONObject) results.get(0);
                                showToast(error.getString("error"));
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    showToast("Notification sent successfully");
                }else{
                    showToast("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    private void listenAvailabilityOfReceiver(){
        database.collection(Constants.KEY_COLLECTION_USERS).document(receiverUSer.id)
                .addSnapshotListener(chat_page.this, (value, error) -> {
                        if(error!=null){
                            return;
        }
                        if(value!=null){
                            if(value.getLong(Constants.KEY_AVAILABILITY) != null){
                                int availability = Objects.requireNonNull(
                                        value.getLong(Constants.KEY_AVAILABILITY)
                                ).intValue();
                                isReceiverAvailable = availability == 1;
                            }
                            receiverUSer.token = value.getString(Constants.KEY_FCM_TOKEN);
                        }
                        if(isReceiverAvailable){
                            binding.textAvailability.setVisibility(View.VISIBLE);
                        } else{
                            binding.textAvailability.setVisibility(View.GONE);
                        }

    });
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null){
            return;
        }
        if(value != null){
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if(count == 0){
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
        if(conversionId == null){
            checkForConversion();
        }
    };

    private void loadReceiverDetails(){
        receiverUSer = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUSer.name);
    }
    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }

    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void addConversion(HashMap<String, Object> conversion){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
    }

    private void updateConversion(String message){
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversionId);
        documentReference.update(
                Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date()
        );
    }

    private void checkForConversion(){
        if(chatMessages.size() != 0 ) {
            checkForConversionRemotely(
                    preferenceManager.getString(Constants.KEY_USER_ID),
                    receiverUSer.id
            );
            checkForConversionRemotely(
                    receiverUSer.id,
                    preferenceManager.getString(Constants.KEY_USER_ID)
            );
        }
    }

    private void checkForConversionRemotely(String senderId, String receiverId){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversionCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversionCompleteListener = task -> {
        if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversionId = documentSnapshot.getId();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        listenAvailabilityOfReceiver();
    }
}