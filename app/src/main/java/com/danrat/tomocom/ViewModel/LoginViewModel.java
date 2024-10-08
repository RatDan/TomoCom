package com.danrat.tomocom.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginViewModel extends ViewModel {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> userLoggedIn = new MutableLiveData<>();

    public LiveData<Boolean> isUserLoggedIn() {
        return userLoggedIn;
    }

    public LiveData<Boolean> isLoginSuccessful() {
        return loginSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public void logIn(String email, String password) {
        loading.setValue(true);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    loading.setValue(false);
                    if (task.isSuccessful()) {
                        loginSuccess.setValue(true);
                    } else {
                        errorMessage.setValue(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void checkIfUserIsLoggedIn() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userLoggedIn.setValue(true);
        } else {
            userLoggedIn.setValue(false);
        }
    }
}

