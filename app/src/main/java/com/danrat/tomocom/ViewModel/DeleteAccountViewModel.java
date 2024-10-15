package com.danrat.tomocom.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class DeleteAccountViewModel extends ViewModel {
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final String userID;
    private final FirebaseAuth auth;
    private final MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> reauthErrorMessage = new MutableLiveData<>();

    public DeleteAccountViewModel () {
        auth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    public LiveData<Boolean> getDeleteSuccess() {
        return deleteSuccess;
    }

    public LiveData<String> getReauthErrorMessage() {
        return reauthErrorMessage;
    }

    public void deleteAccountWithReauthentication(String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            deleteSuccess.setValue(false);
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), password);

        user.reauthenticate(credential)
                .addOnSuccessListener(aVoid -> {
                    deleteAccount(user);
                })
                .addOnFailureListener(e -> {
                    reauthErrorMessage.setValue("Błąd ponownej autoryzacji: niepoprawne hasło.");
                });

    }

    private void deleteAccount(FirebaseUser user) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(userID);
        StorageReference fileRef = storage.getReference().child("profile_images/" + userID);

        documentReference.delete()
                .addOnSuccessListener(unused -> {
                    fileRef.delete()
                        .addOnSuccessListener(unused1 -> {
                            user.delete()
                                .addOnSuccessListener(aVoid -> {
                                    FirebaseAuth.getInstance().signOut();
                                    deleteSuccess.setValue(true);
                                })
                                .addOnFailureListener(e -> {
                                    deleteSuccess.setValue(false);
                                });
                        })
                        .addOnFailureListener(e -> {
                            deleteSuccess.setValue(false);
                        });
                })
                .addOnFailureListener(e -> {
                    deleteSuccess.setValue(false);
                });
    }
}
