package com.danrat.tomocom.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.danrat.tomocom.R;
import com.danrat.tomocom.ViewModel.ClearSkippedViewModel;
import com.danrat.tomocom.ViewModel.DeleteAccountViewModel;

public class ProfileFragment extends PreferenceFragmentCompat {

    private DeleteAccountViewModel deleteAccountViewModel;
    private ClearSkippedViewModel clearSkippedViewModel;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.profile_preferences, rootKey);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        int iconColor;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            iconColor = ContextCompat.getColor(requireContext(), R.color.white);
        } else {
            iconColor = ContextCompat.getColor(requireContext(), R.color.black);
        }

        deleteAccountViewModel = new ViewModelProvider(requireActivity()).get(DeleteAccountViewModel.class);
        clearSkippedViewModel = new ViewModelProvider(requireActivity()).get(ClearSkippedViewModel.class);

        deleteAccountViewModel.getDeleteSuccess().observe(requireActivity(), success -> {
            if (success) {
                Toast.makeText(getContext(), "Konto zostało usunięte.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),MainActivity.class));
                requireActivity().finish();
            } else {
                Toast.makeText(getContext(), "Błąd podczas usuwania konta.", Toast.LENGTH_SHORT).show();
            }
        });

        deleteAccountViewModel.getReauthErrorMessage().observe(requireActivity(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        clearSkippedViewModel.getClearSuccess().observe(requireActivity(), success -> {
            if (success) {
                Toast.makeText(getContext(), "Lista pominiętych użytkowników została wyczyszczona.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Błąd podczas czyszczenia listy pominiętych użytkowników.", Toast.LENGTH_SHORT).show();
            }
        });

        Preference deletePreference = findPreference("delete");
        if (deletePreference != null) {
            deletePreference.setOnPreferenceClickListener(preference -> {
                showReauthDialog();
                return true;
            });
        }

        Preference clearPreference = findPreference("clear");
        if (clearPreference != null) {
            clearPreference.setOnPreferenceClickListener(preference -> {
                clearSkippedViewModel.clearSkipped();
                return false;
            });
            Drawable icon = clearPreference.getIcon();
            if (icon != null) {
                icon.setTint(iconColor);
            }
        }

        Preference profilePreference = findPreference("changeProfilePicture");
        if (profilePreference != null) {
            profilePreference.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(requireContext(), ProfilePictureActivity.class);
                intent.putExtra("mode", "update");
                startActivity(intent);
                return false;
            });
            Drawable icon = profilePreference.getIcon();
            if (icon != null) {
                icon.setTint(iconColor);
            }
        }

        Preference interestPreference = findPreference("interests");
        if (interestPreference != null) {
            interestPreference.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(requireContext(), InterestSelectorActivity.class);
                intent.putExtra("mode", "update");
                startActivity(intent);
                return false;
            });
            Drawable icon = interestPreference.getIcon();
            if (icon != null) {
                icon.setTint(iconColor);
            }
        }

        Preference descriptionPreference = findPreference("description");
        if (descriptionPreference != null) {
            descriptionPreference.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(requireContext(), DescriptionActivity.class);
                intent.putExtra("mode", "update");
                startActivity(intent);
                return false;
            });
            Drawable icon = descriptionPreference.getIcon();
            if (icon != null) {
                icon.setTint(iconColor);
            }
        }

    }

    private void showReauthDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Ponowna autoryzacja");
        builder.setMessage("Aby usunąć konto, podaj hasło do swojego konta.");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Potwierdź", (dialog, which) -> {
            String password = input.getText().toString();
            deleteAccountViewModel.deleteAccountWithReauthentication(password);
        });

        builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}