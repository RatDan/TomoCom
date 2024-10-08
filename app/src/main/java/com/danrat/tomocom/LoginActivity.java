package com.danrat.tomocom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.danrat.tomocom.ViewModel.LoginViewModel;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.loginEmailET);
        passwordEditText = findViewById(R.id.loginPasswordET);
        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);
        TextView remindPasswordTextView = findViewById(R.id.remindPasswordTV);
        progressBar = findViewById(R.id.logInProgressBar);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        loginViewModel.isLoginSuccessful().observe(this, success -> {
            if (success) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                loginViewModel.logIn(email, password);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (LoginActivity.this, SignupActivity.class));
            }
        });

        remindPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        loginViewModel.isUserLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        });

        loginViewModel.checkIfUserIsLoggedIn();
    }
}