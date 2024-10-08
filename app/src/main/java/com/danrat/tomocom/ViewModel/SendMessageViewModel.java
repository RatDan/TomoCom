package com.danrat.tomocom.ViewModel;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.danrat.tomocom.Model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SendMessageViewModel extends ViewModel {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final String userID;

    public SendMessageViewModel() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    public void sendMessage(String message, String uid, String cid) {
        DocumentReference documentReference = firestore.collection("chat_rooms").document(cid);
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        Message newMessage = new Message(userID, uid, message);

        documentReference.update("messages", FieldValue.arrayUnion(newMessage))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Message from " + userID + " to " + uid + " sent");
                    documentReference.update("lastMessage", newMessage)
                            .addOnSuccessListener(aVoid2 -> {
                                Log.d(TAG, "Updated lastMessage from " + userID + " to " + uid + " sent");
                                result.setValue(true);
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error updating lastMessage", e);
                                result.setValue(false);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error sending message", e);
                    result.setValue(false);
                });

    }
}
