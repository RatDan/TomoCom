package com.danrat.tomocom.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CurrentUserViewModel extends ViewModel {
    private final MutableLiveData<DocumentSnapshot> documentSnapshotLiveData = new MutableLiveData<>();

    public LiveData<DocumentSnapshot> getUserDocument() {
        return documentSnapshotLiveData;
    }

    public void loadUserDocument (String userID) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").document(userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        documentSnapshotLiveData.setValue(documentSnapshot);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "get failed with ", e);
                });
    }


}
