package com.danrat.tomocom.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomsViewModel extends ViewModel {
    private final MutableLiveData<List<Chat>> chatDataList = new MutableLiveData<>();

    public LiveData<List<Chat>> getChatRooms() { return chatDataList; }

    public void fetchChatData (List<String> friends) {
        if (chatDataList.getValue() != null && !chatDataList.getValue().isEmpty())
            return;
        if (friends.isEmpty())
           return;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("chat_rooms")
                .whereArrayContainsAny("members",friends)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w("Firestore", "Listen failed", e);
                        return;
                    }
                    if (snapshot != null && !snapshot.isEmpty()) {
                        List<Chat> chatRooms = new ArrayList<>();
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Chat chat = document.toObject(Chat.class);
                            chatRooms.add(chat);
                        }
                        chatDataList.setValue(chatRooms);

                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d("TAG", "New Chat: " + dc.getDocument().toObject(Message.class));
                                    break;
                                case MODIFIED:
                                    Log.d("TAG", "Modified Chat: " + dc.getDocument().toObject(Message.class));
                                    break;
                                case REMOVED:
                                    Log.d("TAG", "Removed Chat: " + dc.getDocument().toObject(Message.class));
                                    break;
                            }
                        }
                    } else {
                        List<Chat> chatRooms = new ArrayList<>();
                        chatDataList.setValue(chatRooms);
                    }
                });
    }
}
