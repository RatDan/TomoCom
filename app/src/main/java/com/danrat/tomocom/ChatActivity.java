package com.danrat.tomocom;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.danrat.tomocom.Adapter.MessageListAdapter;
import com.danrat.tomocom.Model.Chat;
import com.danrat.tomocom.Model.ChatRoomsViewModel;
import com.danrat.tomocom.Model.CurrentChatViewModel;
import com.danrat.tomocom.Model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.activity.OnBackPressedCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    private static final String USERNAME_ARG = "username";
    private static final String UID_ARG = "uid";
    private static final String CID_ARG = "cid";

    private List<Message> messages;
    private String username;
    private String uid;
    private String cid;
    private RecyclerView recyclerView;
    private MessageListAdapter adapter;
    private EditText messageET;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private final String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messages = new ArrayList<>();

        recyclerView = findViewById(R.id.chatRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageET = findViewById(R.id.messageET);
        ImageButton sendMessageButton = findViewById(R.id.sendMessageButton);

        if (getIntent() != null) {
            username = getIntent().getStringExtra(USERNAME_ARG);
            uid = getIntent().getStringExtra(UID_ARG);
            cid = getIntent().getStringExtra(CID_ARG);
        }

        CurrentChatViewModel currentChatViewModel = new ViewModelProvider(this).get(CurrentChatViewModel.class);
        currentChatViewModel.getChatRoom().observe(this, chat -> {
            if (chat != null) {
                messages.clear();
                messages = chat.getMessages();
                if (adapter == null) {
                    adapter = new MessageListAdapter(messages, username, userID, uid);
                    recyclerView.setAdapter(adapter);
                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                } else {
                    adapter.updateData(messages);
                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });

        currentChatViewModel.fetchChatData(cid);

        DocumentReference documentReference = fireStore.collection("chat_rooms").document(cid);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(documentReference);
            }
        });
        messageET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage(documentReference);
                    handled=true;
                }
                return handled;
            }
        });

        TextView textView = findViewById(R.id.usernameTV);
        textView.append(" " + username);

    }

    private void sendMessage (DocumentReference documentReference) {
        String message = messageET.getText().toString().trim();
        documentReference
                .update("messages", FieldValue.arrayUnion(new Message(userID,uid, message)))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Message from " + userID +" to " + uid + " sent");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG,"Error sending message", e);
                });
        documentReference
                .update("lastMessage", new Message(userID,uid, message))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Updated lastMessage from " + userID +" to " + uid + " sent");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG,"Error updating lastMessage", e);
                });
        messageET.getText().clear();
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1); //TODO:
    }
}