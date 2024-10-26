package com.danrat.tomocom.ViewModel;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfilePictureViewModel extends ViewModel {

    private final MutableLiveData<Uri> profileImageUri = new MutableLiveData<>();

    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isImageUploaded = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final FirebaseFirestore fireStore;
    private final FirebaseStorage firebaseStorage;
    private final String userID;
    private String currentImageUrl;

    public ProfilePictureViewModel() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        loadProfileImage();
    }

    public LiveData<Uri> getProfileImageUri() {
        return profileImageUri;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public LiveData<Boolean> getIsImageUploaded() {
        return isImageUploaded;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public void loadProfileImage() {
        DocumentReference documentReference = fireStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                currentImageUrl = documentSnapshot.getString("profileImageUrl");
                if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
                    profileImageUri.setValue(Uri.parse(currentImageUrl));
                }
            }
        }).addOnFailureListener(e -> toastMessage.setValue("Błąd ładowania zdjęcia profilowego"));
    }

    public void uploadImageToFirebase(Uri imageUri) {
        if (imageUri != null) {
            loading.setValue(true);
            StorageReference storageReference = firebaseStorage.getReference("profile_images/" + userID);

            if (currentImageUrl != null) {
                StorageReference oldImageRef = firebaseStorage.getReferenceFromUrl(currentImageUrl);
                oldImageRef.delete()
                        .addOnSuccessListener(aVoid -> Log.d("DeleteImage", "Previous profile image deleted"))
                        .addOnFailureListener(e -> Log.e("DeleteImageError", "Error deleting previous image", e));
            }

            storageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveProfileImageUrl(userID, imageUrl);
                    }))
                    .addOnFailureListener(e -> toastMessage.setValue("Błąd przesyłania zdjęcia"));
        }
    }

    private void saveProfileImageUrl(String userId, String imageUrl) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("profileImageUrl", imageUrl);

        fireStore.collection("users").document(userId)
                .set(userMap, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    loading.setValue(false);
                    isImageUploaded.setValue(true);
                    currentImageUrl = imageUrl;
                    toastMessage.setValue("Zdjęcie profilowe zapisane pomyślnie");
                })
                .addOnFailureListener(e -> toastMessage.setValue("Błąd zapisywania zdjęcia profilowego"));
    }
}
