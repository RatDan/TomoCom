package com.danrat.tomocom.ViewModel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class DescriptionViewModel extends ViewModel {

    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private final String userID;

    //private final MutableLiveData<String> updatedDescription = new MutableLiveData<>();

    public DescriptionViewModel () {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    public void updateDescription(String description) {
        DocumentReference documentReference = fireStore.collection("users").document(userID);

        documentReference.update("description", description)
                .addOnSuccessListener(aVoid -> Log.d("UpdateDescription","Description updated."))
                .addOnFailureListener(e -> Log.e("UpdateDescriptionError","Description update error: ", e));
    }

}
