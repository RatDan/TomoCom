package com.danrat.tomocom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        Preference logOut = findPreference("logout");
        if (logOut!=null) {
            logOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    Toast.makeText(getActivity(), "Wylogowano pomyślnie!",Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        };
    }
}