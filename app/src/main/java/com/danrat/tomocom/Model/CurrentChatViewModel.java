package com.danrat.tomocom.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

public class CurrentChatViewModel extends ViewModel {
    private final MutableLiveData<Chat> chatData = new MutableLiveData<>();

    public LiveData<Chat> getChatRoom() { return chatData; }

    public void fetchChatData (String cid) {
        if (cid == null || cid.isEmpty())
            return;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
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
}
