package com.danrat.tomocom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.danrat.tomocom.Model.ChatRoomsViewModel;
import com.danrat.tomocom.Model.User;
import com.danrat.tomocom.Model.CurrentUserViewModel;
import com.danrat.tomocom.Model.UserListViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private boolean backPressedOnce = false;
    private final Handler backPressHandler = new Handler();
    private final Runnable backPressRunnable = new Runnable() {
        @Override
        public void run() {
            backPressedOnce = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //FirebaseAuth auth = FirebaseAuth.getInstance();

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
        bottomNavigationView.setSelectedItemId(R.id.nav_find);
        UserListViewModel userListViewModel = new ViewModelProvider(this).get(UserListViewModel.class);
        ChatRoomsViewModel chatRoomsViewModel = new ViewModelProvider(this).get(ChatRoomsViewModel.class);
        //CurrentUserViewModel currentUserViewModel = new ViewModelProvider(this).get(CurrentUserViewModel.class);

        //String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        //currentUserViewModel.loadUserDocument(userID);

        /* currentUserViewModel.getUserDocument().observe(this, documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            if (user != null)
                Toast.makeText(HomeActivity.this, "Witaj, " + user.getUsername(),Toast.LENGTH_SHORT).show();
        }); */
    }


    SettingsFragment settingsFragment = new SettingsFragment();
    FindFragment findFragment = new FindFragment();
    FriendsFragment friendsFragment = new FriendsFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();

    @SuppressLint("NonConstantResourceId")
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {
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

}