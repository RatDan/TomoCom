package com.danrat.tomocom.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MessagesViewModel extends ViewModel {
    /*private final MutableLiveData<List<Message>> messageDataList = new MutableLiveData<>();

    public LiveData<List<Message>> getMessages() { return messageDataList; }

    public void fetchChatData () {
        if (messageDataList.getValue() != null && !messageDataList.getValue().isEmpty())
            return;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("chat_rooms")
                .whereArrayContainsAny("members",friends)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Chat> chatRooms = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Chat chat = document.toObject(Chat.class);
                                chatRooms.add(chat);
                            }
                            messageDataList.setValue(chatRooms);
                        } else {
                            Log.d(TAG, "get error: ", task.getException());
                        }
                    }
                });
    }*/
}
