package com.danrat.tomocom.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.danrat.tomocom.FragmentUtils;
import com.danrat.tomocom.R;
import com.danrat.tomocom.ViewModel.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private boolean backPressedOnce = false;
    private final Handler backPressHandler = new Handler();
    private final Runnable backPressRunnable = () -> backPressedOnce = false;
    private Integer selectedNavItemId = R.id.nav_find;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState != null) {
            selectedNavItemId = savedInstanceState.getInt("selected_nav_item_id", R.id.nav_find);

            switch (selectedNavItemId) {
                case R.id.nav_friends:
                    FragmentUtils.replaceFragment(fragmentManager, friendsFragment);
                    break;
                case R.id.nav_profile:
                    FragmentUtils.replaceFragment(fragmentManager, profileFragment);
                    break;
                case R.id.nav_settings:
                    FragmentUtils.replaceFragment(fragmentManager, settingsFragment);
                    break;
                default:
                    FragmentUtils.replaceFragment(fragmentManager, findFragment);
                    break;
            }
        } else {
            FragmentUtils.replaceFragment(fragmentManager, findFragment);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkModeEnabled = preferences.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(isDarkModeEnabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.checkUserData();

        homeViewModel.isProfilePictureMissing().observe(this, isMissing -> {
            if (isMissing) {
                Intent intent = new Intent(HomeActivity.this, ProfilePictureActivity.class);
                intent.putExtra("ButtonSave", "Zapisz");
                startActivity(intent);
                finish();
            }
        });

        homeViewModel.areInterestsMissing().observe(this, areMissing -> {
            if (areMissing) {
                Intent intent = new Intent(HomeActivity.this, InterestSelectorActivity.class);
                intent.putExtra("ButtonSave", "Zapisz");
                startActivity(intent);
                finish();
            }
        });

        homeViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(HomeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressedOnce) {
                    finish();
                } else {
                    backPressedOnce = true;
                    Toast.makeText(HomeActivity.this, "Naciśnij jeszcze raz, aby wyjść z aplikacji", Toast.LENGTH_SHORT).show();
                    backPressHandler.postDelayed(backPressRunnable, 2000);
                }
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(this);

    }

    SettingsFragment settingsFragment = new SettingsFragment();
    FindFragment findFragment = new FindFragment();
    FriendsFragment friendsFragment = new FriendsFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();

    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        selectedNavItemId = item.getItemId();

        switch (item.getItemId()) {
            case R.id.nav_find:
                FragmentUtils.replaceFragment(fragmentManager, findFragment);
                return true;
            case R.id.nav_friends:
                FragmentUtils.replaceFragment(fragmentManager, friendsFragment);
                return true;
            case R.id.nav_profile:
                FragmentUtils.replaceFragment(fragmentManager, profileFragment);
                return true;
            case R.id.nav_settings:
                FragmentUtils.replaceFragment(fragmentManager, settingsFragment);
                return true;
        }

        FragmentUtils.replaceFragment(fragmentManager, findFragment);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backPressHandler.removeCallbacks(backPressRunnable);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected_nav_item_id", selectedNavItemId);
    }


}