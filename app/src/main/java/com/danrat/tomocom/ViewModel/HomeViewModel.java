package com.danrat.tomocom.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.danrat.tomocom.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class HomeViewModel extends ViewModel {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isProfilePictureMissing = new MutableLiveData<>();
    private final MutableLiveData<Boolean> areInterestsMissing = new MutableLiveData<>();

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<Boolean> isProfilePictureMissing() {
        return isProfilePictureMissing;
    }

    public LiveData<Boolean> areInterestsMissing() {
        return areInterestsMissing;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void checkUserData() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userID = currentUser.getUid();
            fireStore.collection("users").document(userID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            User user = task.getResult().toObject(User.class);
                            if (user != null) {
                                userLiveData.setValue(user);
                                if (user.getProfileImageUrl() == null || user.getProfileImageUrl().isEmpty()) {
                                    isProfilePictureMissing.setValue(true);
                                } else {
                                    isProfilePictureMissing.setValue(false);
                                }

                                if (user.getInterests() == null || user.getInterests().isEmpty()) {
                                    areInterestsMissing.setValue(true);
                                } else {
                                    areInterestsMissing.setValue(false);
                                }
                            }
                        } else {
                            errorMessage.setValue("Błąd pobierania danych użytkownika: " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        } else {
            errorMessage.setValue("Użytkownik niezalogowany.");
        }
    }
}

