package com.danrat.tomocom.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.danrat.tomocom.Adapter.MessageListAdapter;
import com.danrat.tomocom.R;
import com.danrat.tomocom.ViewModel.CurrentChatViewModel;
import com.danrat.tomocom.Model.Message;
import com.danrat.tomocom.ViewModel.SendMessageViewModel;
import com.google.common.base.Strings;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String USERNAME_ARG = "username";
    private static final String UID_ARG = "uid";
    private static final String CID_ARG = "cid";
    private static final String PROFILEPICTUREURL_ARG = "profilePictureUrl";
    private static final String DESCRIPTION_ARG = "description";

    private List<Message> messages;
    private String username;
    private String uid;
    private String cid;
    private String profilePictureUrl;
    private String description;
    private RecyclerView recyclerView;
    private MessageListAdapter adapter;
    private EditText messageET;
    private ImageView profileImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messages = new ArrayList<>();

        recyclerView = findViewById(R.id.chatRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageET = findViewById(R.id.messageET);
        ImageButton sendMessageButton = findViewById(R.id.sendMessageButton);
        profileImageView = findViewById(R.id.profileIV);

        if (getIntent() != null) {
            username = getIntent().getStringExtra(USERNAME_ARG);
            uid = getIntent().getStringExtra(UID_ARG);
            cid = getIntent().getStringExtra(CID_ARG);
            description = getIntent().getStringExtra(DESCRIPTION_ARG);
            profilePictureUrl = getIntent().getStringExtra(PROFILEPICTUREURL_ARG);
        }

        TextView usernameTextView = findViewById(R.id.usernameTV);
        usernameTextView.setText(username);

        TextView descriptionTextView = findViewById(R.id.descriptionTV);
        descriptionTextView.setText(description);

        CurrentChatViewModel currentChatViewModel = new ViewModelProvider(this).get(CurrentChatViewModel.class);

        currentChatViewModel.getImageUrl().observe(this, url -> {
            if (!Strings.isNullOrEmpty(url)) {
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.nav_profile)
                        .into(profileImageView);
            }
        });

        String userID = currentChatViewModel.getUserID();
        currentChatViewModel.getChatRoom().observe(this, chat -> {
            if (chat != null) {
                messages.clear();
                messages = chat.getMessages();
                currentChatViewModel.loadImage(profilePictureUrl);
                if (adapter == null) {
                    adapter = new MessageListAdapter(messages, username, userID, uid);
                    recyclerView.setAdapter(adapter);
                    if (!messages.isEmpty())
                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                } else {
                    adapter.updateData(messages);
                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });

        currentChatViewModel.fetchChatData(cid);

        SendMessageViewModel sendMessageViewModel = new ViewModelProvider(this).get(SendMessageViewModel.class);
        sendMessageButton.setOnClickListener(v -> {
            if (!messageET.getText().toString().isEmpty() || messageET.getText() == null) {
                String message = messageET.getText().toString().trim();
                sendMessageViewModel.sendMessage(message, uid, cid);
                messageET.getText().clear();
                if (!messages.isEmpty())
                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        });
        messageET.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND && (!messageET.getText().toString().isEmpty() || messageET.getText() == null)) {
                String message = messageET.getText().toString().trim();
                sendMessageViewModel.sendMessage(message, uid, cid);
                messageET.getText().clear();
                if (!messages.isEmpty())
                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                handled = true;
            }
            return handled;
        });
    }
}