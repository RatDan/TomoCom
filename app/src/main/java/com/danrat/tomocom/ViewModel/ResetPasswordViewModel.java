package com.danrat.tomocom.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetPasswordViewModel extends ViewModel {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final MutableLiveData<Boolean> resetSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public LiveData<Boolean> isLinkSent() {
        return resetSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public void resetPassword (String email) {
        loading.setValue(true);
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    loading.setValue(false);
                    if (task.isSuccessful()) {
                        resetSuccess.setValue(true);
                    } else {
                        errorMessage.setValue(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

}
