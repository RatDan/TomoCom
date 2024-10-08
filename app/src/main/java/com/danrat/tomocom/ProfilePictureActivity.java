package com.danrat.tomocom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.danrat.tomocom.ViewModel.ProfilePictureViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.squareup.picasso.Picasso;

public class ProfilePictureActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private Uri imageUri;
    private ProfilePictureViewModel profilePictureViewModel;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    profileImageView.setImageURI(imageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        profileImageView = findViewById(R.id.profileImageView);
        Button selectImageButton = findViewById(R.id.selectImageButton);
        Button nextButton = findViewById(R.id.nextButton);

        profilePictureViewModel = new ViewModelProvider(this).get(ProfilePictureViewModel.class);

        profilePictureViewModel.getProfileImageUri().observe(this, uri -> {
            if (uri != null) {
                loadImage(uri.toString());
            }
        });

        profilePictureViewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(ProfilePictureActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        profilePictureViewModel.getIsImageUploaded().observe(this, isUploaded -> {
            if (isUploaded) {
                handleActivity();
            }
        });

        selectImageButton.setOnClickListener(v -> openImageChooser());

        nextButton.setOnClickListener(v -> {
            if (imageUri == null && profilePictureViewModel.getProfileImageUri().getValue() == null) {
                Toast.makeText(ProfilePictureActivity.this, "Proszę dodać zdjęcie profilowe.", Toast.LENGTH_SHORT).show();
            } else if (imageUri != null) {
                profilePictureViewModel.uploadImageToFirebase(imageUri);
            } else {
                handleActivity();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void loadImage(String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.nav_profile)
                .into(profileImageView);
    }

    private void handleActivity() {
        if (getIntent() != null)
            finish();
        else
            startActivity(new Intent(getApplicationContext(), DescriptionActivity.class));
    }
}