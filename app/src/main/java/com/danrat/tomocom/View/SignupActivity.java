package com.danrat.tomocom.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.danrat.tomocom.Model.User;
import com.danrat.tomocom.R;
import com.danrat.tomocom.ViewModel.SignUpViewModel;

public class SignupActivity extends AppCompatActivity {

    private EditText emailET;
    private EditText passwordET;
    private EditText usernameET;
    private EditText ageET;
    private ProgressBar progressBar;
    private SignUpViewModel signUpViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        usernameET = findViewById(R.id.username);
        ageET = findViewById(R.id.age);
        progressBar = findViewById(R.id.signUpProgressBar);
        Button registerButton = findViewById(R.id.registerButton);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        signUpViewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        signUpViewModel.isAccountCreated().observe(this, isCreated -> {
            if (isCreated) {
                Toast.makeText(SignupActivity.this, "Utworzono konto", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ProfilePictureActivity.class));
                finish();
            }
        });

        signUpViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            String email = emailET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();
            String username = usernameET.getText().toString().trim();
            int age = Integer.parseInt(ageET.getText().toString().trim());

            User newUser = new User(username, age);
            signUpViewModel.createAccount(email, password, newUser);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        signUpViewModel.checkIfUserLoggedIn();
    }
}