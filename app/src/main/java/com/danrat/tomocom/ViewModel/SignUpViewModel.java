package com.danrat.tomocom.ViewModel;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.danrat.tomocom.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUpViewModel extends ViewModel {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    private final MutableLiveData<Boolean> accountCreated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<Boolean> isAccountCreated() {
        return accountCreated;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void createAccount(String email, String password, User newUser) {
        if (!isValidEmail(email)) {
            errorMessage.setValue("Email jest niepoprawny!");
            return;
        }

        if (TextUtils.isEmpty(newUser.getUsername())) {
            errorMessage.setValue("Nazwa użytkownika jest wymagana!");
            return;
        }

        if (isAgeEmpty(newUser.getAge())) {
            errorMessage.setValue("Wiek jest wymagany!");
            return;
        }

        if (newUser.getAge() < 13) {
            errorMessage.setValue("Musisz mieć minimum 13 lat!");
            return;
        }

        if (!validatePassword(password)) {
            return;
        }

        loading.setValue(true);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    loading.setValue(false);
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = auth.getCurrentUser();
                        String userID = currentUser.getUid();
                        newUser.setUid(userID);
                        saveUserInFirestore(newUser);
                    } else {
                        errorMessage.setValue("Error: " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    private void saveUserInFirestore(User newUser) {
        fireStore.collection("users").document(newUser.getUid())
                .set(newUser)
                .addOnSuccessListener(unused -> {
                    accountCreated.setValue(true);
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Failed to save user: " + e.getMessage());
                });
    }

    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            errorMessage.setValue("Hasło jest wymagane!");
            return false;
        }

        if (password.length() < 8) {
            errorMessage.setValue("Hasło musi składać się z minimum ośmiu znaków!");
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            errorMessage.setValue("Hasło nie posiada wielkiej litery!");
            return false;
        }

        if (!password.matches(".*[0-9].*")) {
            errorMessage.setValue("Hasło nie posiada cyfry!");
            return false;
        }

        if (!password.matches(".*[!@$%^*+#].*")) {
            errorMessage.setValue("Hasło nie posiada znaku specjalnego!");
            return false;
        }

        if (!password.matches(".*[a-zA-Z0-9!@$%^*+#].*")) {
            errorMessage.setValue("Hasło posiada niedozwolony znak!");
            return false;
        }

        return true;
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean isAgeEmpty(Integer num) {
        return num == null || num == 0;
    }

    public void checkIfUserLoggedIn() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            accountCreated.setValue(true);
        }
    }
}

