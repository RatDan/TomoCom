package com.danrat.tomocom.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.danrat.tomocom.Model.Interests;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InterestsViewModel extends ViewModel {
    private static final int maxInterests = 5;
    private static final int minInterests = 3;

    private final MutableLiveData<List<Interests>> selectedInterests = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> limitExceeded = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isButtonEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> canSelectMoreInterests = new MutableLiveData<>(true);
    private final MutableLiveData<String> message = new MutableLiveData<>("");

    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private final String userID;

    public InterestsViewModel () {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    public LiveData<String> getMessage() { return message; }
    public LiveData<Boolean> isButtonEnabled() { return isButtonEnabled; }
    public LiveData<Boolean> canSelectMoreInterests() { return canSelectMoreInterests; }
    public LiveData<List<Interests>> getSelectedInterests() { return selectedInterests; }

    public void toggleInterest(Interests interest, boolean isChecked) {
        List<Interests> currentInterests = selectedInterests.getValue();
        if (currentInterests != null) {
            if (isChecked && currentInterests.size() < maxInterests) {
                currentInterests.add(interest);
            } else if (!isChecked) {
                currentInterests.remove(interest);
            }


            limitExceeded.setValue(currentInterests.size() >= maxInterests);
            canSelectMoreInterests.setValue(currentInterests.size() < maxInterests);

            isButtonEnabled.setValue(currentInterests.size() >= minInterests);

            selectedInterests.setValue(currentInterests);
        }
    }


    public void updateInterests(OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        List<Interests> currentInterests = selectedInterests.getValue();

        DocumentReference documentReference = fireStore.collection("users").document(userID);

        documentReference.update("interests", currentInterests)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }
}
