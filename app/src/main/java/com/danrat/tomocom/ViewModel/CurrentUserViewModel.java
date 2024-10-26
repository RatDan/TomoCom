package com.danrat.tomocom.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CurrentUserViewModel extends ViewModel {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final MutableLiveData<Boolean> userLoggedIn = new MutableLiveData<>();

    public LiveData<Boolean> isUserLoggedIn() {
        return userLoggedIn;
    }

    public void checkIfUserLoggedIn() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userLoggedIn.setValue(true);
        }
    }
}
