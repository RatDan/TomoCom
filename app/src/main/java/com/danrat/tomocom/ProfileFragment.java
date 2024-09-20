package com.danrat.tomocom;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class ProfileFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.profile_preferences, rootKey);

        //Preference preference = findPreference("delete");
    }
}