package com.danrat.tomocom.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ClearSkippedViewModel extends ViewModel {
    private final String userID;
    private final FirebaseAuth auth;
    private final MutableLiveData<Boolean> clearSuccess = new MutableLiveData<>();

    public ClearSkippedViewModel() {
        auth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    public LiveData<Boolean> getClearSuccess() {
        return clearSuccess;
    }

    public void clearSkipped() {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(userID);

        docRef.update("skipped", FieldValue.delete())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        clearSuccess.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        clearSuccess.setValue(false);
                    }
                });
    }
}
