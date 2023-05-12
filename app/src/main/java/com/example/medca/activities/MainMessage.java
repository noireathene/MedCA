package com.example.medca.activities;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.medca.R;
import com.example.medca.adapters.RecentConversationsAdapter;
import com.example.medca.databinding.ActivityMainMessageBinding;
import com.example.medca.listeners.ConversionListener;
import com.example.medca.models.ChatMessage;
import com.example.medca.models.User;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainMessage extends BaseActivity implements ConversionListener {
    ImageButton imageback;
    private PreferenceManager preferenceManager;
    private ActivityMainMessageBinding binding;
    private List<ChatMessage> conversations;
    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //This line hides the action bar
        binding = ActivityMainMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imageback = findViewById(R.id.imageBack4);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), homepage_doctor.class);
                startActivity(intent);
            }
        });
        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
        //get token
        getToken();
        listenConversations();

         //click listener for user list
        binding.fabNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), display_user.class);
                startActivity(intent);
            }   
        });
    }


    private void init(){
        conversations = new ArrayList<>();
        conversationsAdapter = new RecentConversationsAdapter(conversations, this);
        binding.conversationsRecyclerView.setAdapter(conversationsAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void listenConversations(){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }


    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = senderId;
                    chatMessage.receiverId = receiverId;
                    if (preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)){
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    } else {
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    }
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversations.add(chatMessage);
                } else if(documentChange.getType() == DocumentChange.Type.MODIFIED) {
                        for (int i = 0; i < conversations.size(); i++){
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if(conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)){
                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            conversationsAdapter.notifyDataSetChanged();
            binding.conversationsRecyclerView.smoothScrollToPosition(0);
            binding.conversationsRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    };

    //get token
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    //update FCM token
    private void updateToken(String token){
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Unable to update token"));
    }

    @Override
    public void onConversionClicked(User user){
        Intent intent = new Intent(getApplicationContext(), chat_page.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
    }
}