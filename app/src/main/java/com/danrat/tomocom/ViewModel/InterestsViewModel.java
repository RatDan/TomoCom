package com.danrat.tomocom.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InterestsViewModel extends ViewModel {
    private final String userID;
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private final MutableLiveData<Map<String, String>> selectedCategoriesWithSubinterests = new MutableLiveData<>(new HashMap<>());
    private final MutableLiveData<Boolean> canSelectMoreInterests = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> isButtonEnabled = new MutableLiveData<>(false);

    private final int maxCategories = 5;
    private final int minCategories = 3;

    public InterestsViewModel() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    public LiveData<Map<String, String>> getSelectedCategoriesWithSubinterests() {
        return selectedCategoriesWithSubinterests;
    }

    public LiveData<Boolean> isButtonEnabled() {
        return isButtonEnabled;
    }

    public void toggleInterest(String category, String subcategory, boolean isChecked) {
        Map<String, String> currentSelections = selectedCategoriesWithSubinterests.getValue();
        if (currentSelections != null) {
            if (isChecked) {
                if (currentSelections.size() < maxCategories) {
                    currentSelections.put(category, subcategory);
                } else {
                    canSelectMoreInterests.setValue(false);
                }
            } else {
                currentSelections.remove(category);
            }

            canSelectMoreInterests.setValue(currentSelections.size() < maxCategories);
            isButtonEnabled.setValue(currentSelections.size() >= minCategories);

            selectedCategoriesWithSubinterests.setValue(currentSelections);
        }
    }


    public void updateInterests(OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        Map<String, String> currentSelections = selectedCategoriesWithSubinterests.getValue();

        if (currentSelections != null && currentSelections.size() >= minCategories) {
            DocumentReference documentReference = fireStore.collection("users").document(userID);

            List<String> interests = new ArrayList<>(currentSelections.keySet());
            List<String> subinterests = new ArrayList<>(currentSelections.values());

            Map<String, Object> updates = new HashMap<>();
            updates.put("interests", interests);
            updates.put("subinterests", subinterests);

            documentReference.update(updates)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Minimum 3 interests in different categories must be selected"));
        }
    }

}
