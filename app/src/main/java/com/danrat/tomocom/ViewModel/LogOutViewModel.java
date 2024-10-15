package com.danrat.tomocom.ViewModel;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class LogOutViewModel extends ViewModel {

    public void logOut () {
        FirebaseAuth.getInstance().signOut();
    }
}
