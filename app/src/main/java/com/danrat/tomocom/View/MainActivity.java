package com.danrat.tomocom.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.danrat.tomocom.R;
import com.danrat.tomocom.ViewModel.CurrentUserViewModel;

public class MainActivity extends AppCompatActivity {

    private CurrentUserViewModel currentUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TomoComm);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkModeEnabled = preferences.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(isDarkModeEnabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUserViewModel = new ViewModelProvider(this).get(CurrentUserViewModel.class);

        currentUserViewModel.isUserLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                    finishAffinity();
            }
        });

        Button registerButton = findViewById(R.id.startButton);
        Button loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
            finish();
        });

        loginButton.setOnClickListener(v ->
        {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUserViewModel.checkIfUserLoggedIn();
    }

}