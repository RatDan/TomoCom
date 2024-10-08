package com.danrat.tomocom.ViewModel;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.danrat.tomocom.Model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class CurrentChatViewModel extends ViewModel {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final String userID;
    private final MutableLiveData<Chat> chatData = new MutableLiveData<>();
    private final MutableLiveData<String> imageUrl = new MutableLiveData<>();

    public CurrentChatViewModel() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    public LiveData<Chat> getChatRoom() { return chatData; }

    public String getUserID() { return userID; }

    public LiveData<String> getImageUrl() {
        return imageUrl;
    }

    public void fetchChatData (String cid) {
        if (cid == null || cid.isEmpty())
            return;

        firestore.collection("chat_rooms")
                .document(cid)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w("Firestore", "Listen failed", e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, snapshot.getId() + " => " + snapshot.getData());
                        Chat chat = snapshot.toObject(Chat.class);
                        chatData.setValue(chat);
                    } else {
                        Log.d(TAG, "No chat found with CID: " + cid);
                        chatData.setValue(null);
                    }
                });
    }

    public void loadImage(String url) {
        imageUrl.setValue(url);
    }
}

