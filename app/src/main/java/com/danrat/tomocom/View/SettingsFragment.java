package com.danrat.tomocom.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.danrat.tomocom.R;
import com.danrat.tomocom.ViewModel.LogOutViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        int iconColor;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            iconColor = ContextCompat.getColor(requireContext(), R.color.white);
        } else {
            iconColor = ContextCompat.getColor(requireContext(), R.color.black);
        }

        LogOutViewModel logOutViewModel = new ViewModelProvider(requireActivity()).get(LogOutViewModel.class);

        SwitchPreferenceCompat darkModePreference = findPreference("dark_mode");
        if (darkModePreference != null) {
            darkModePreference.setChecked(isDarkModeEnabled());

            darkModePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isDarkModeEnabled = (Boolean) newValue;
                setDarkMode(isDarkModeEnabled);
                return true;
            });

            Drawable icon = darkModePreference.getIcon();
            if (icon != null) {
                icon.setTint(iconColor);
            }
        }

        Preference matchPreference = findPreference("match");
        if (matchPreference != null) {
            Drawable icon = matchPreference.getIcon();
            if (icon != null) {
                icon.setTint(iconColor);
            }
        }

        Preference logOutPreference = findPreference("logout");
        if (logOutPreference != null) {
            logOutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    logOutViewModel.logOut();
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    Toast.makeText(getActivity(), "Wylogowano pomyślnie!",Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            Drawable icon = logOutPreference.getIcon();
            if (icon != null) {
                icon.setTint(iconColor);
            }
        }

        Preference aboutPreference = findPreference("about");
        if (aboutPreference != null) {
            aboutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    startActivity(new Intent(getActivity(),AboutUsActivity.class));
                    return true;
                }
            });
            Drawable icon = aboutPreference.getIcon();
            if (icon != null) {
                icon.setTint(iconColor);
            }
        }

        EditTextPreference agePreference = findPreference("age");
        if (agePreference != null) {
            agePreference.setOnBindEditTextListener(editText -> {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.getText().clear();
            });

            agePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    String ageString = (String) newValue;
                    try {
                        int age = Integer.parseInt(ageString);
                        if (age < 13 || age > 120) {
                            agePreference.setText(String.valueOf(Integer.parseInt("13")));
                            agePreference.setSummaryProvider(preference1 -> "13");
                            Toast.makeText(getContext(), "Wiek musi być większy od 13 i mniejszy od 120", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        agePreference.setText(String.valueOf(Integer.parseInt("13")));
                        Toast.makeText(getContext(), "Podaj poprawny numer", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    return true;
                }
            });
            Drawable icon = agePreference.getIcon();
            if (icon != null) {
                icon.setTint(iconColor);
            }
        }
    }

    private boolean isDarkModeEnabled() {
        SharedPreferences preferences = getPreferenceManager().getSharedPreferences();

        if (preferences != null)
            return preferences.getBoolean("dark_mode", false);

        return false;
    }

    private void setDarkMode(boolean isEnabled) {
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}