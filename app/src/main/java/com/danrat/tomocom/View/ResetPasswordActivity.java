package com.danrat.tomocom.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.danrat.tomocom.R;
import com.danrat.tomocom.ViewModel.ResetPasswordViewModel;

public class ResetPasswordActivity extends AppCompatActivity {

    private ResetPasswordViewModel resetPasswordViewModel;
    private EditText emailEditText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailEditText = findViewById(R.id.resetPasswordEmailET);
        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);
        Button loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.resetPasswordProgressBar);

        resetPasswordViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);

        resetPasswordViewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        resetPasswordViewModel.isLinkSent().observe(this, success -> {
           if (success) {
               Toast.makeText(ResetPasswordActivity.this, "Link do resetowania hasła wysłano pomyślnie!",Toast.LENGTH_SHORT).show();
           }
           else {
               Toast.makeText(ResetPasswordActivity.this, "Błąd: " + resetPasswordViewModel.getErrorMessage(),Toast.LENGTH_SHORT).show();
           }
        });

        loginButton.setOnClickListener(v -> startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class)));

        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            resetPasswordViewModel.resetPassword(email);
        });
    }
}