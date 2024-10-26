package com.danrat.tomocom.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.danrat.tomocom.R;
import com.danrat.tomocom.ViewModel.ProfilePictureViewModel;

import com.squareup.picasso.Picasso;

public class ProfilePictureActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private Uri imageUri;
    private ProfilePictureViewModel profilePictureViewModel;
    private ProgressBar progressBar;
    String mode;

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

        mode = getIntent().getStringExtra("mode");
        profileImageView = findViewById(R.id.profileImageView);
        progressBar = findViewById(R.id.profilePictureProgressBar);
        Button selectImageButton = findViewById(R.id.selectImageButton);
        Button nextButton = findViewById(R.id.nextButton);
        if (mode != null)
            nextButton.setText(R.string.button_save);

        profilePictureViewModel = new ViewModelProvider(this).get(ProfilePictureViewModel.class);

        profilePictureViewModel.getProfileImageUri().observe(this, uri -> {
            if (uri != null) {
                loadImage(uri.toString());
            }
        });

        profilePictureViewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
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
        if ("update".equals(mode))
            finish();
        else if ("missing".equals(mode))
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        else {
            startActivity(new Intent(getApplicationContext(), DescriptionActivity.class));
            finish();
        }
    }
}