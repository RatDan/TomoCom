package com.danrat.tomocom;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileDialogFragment extends DialogFragment {
    private static final String userNameArg = "username";
    private static final String ageArg = "age";
    private static final String interestsArg = "interests";
    private static final String descriptionArg = "description";
    private static final String profileImageUrlArg = "profileImageUrl";

    private String userName;
    private int age;
    private String interests;
    private String description;
    private String profileImageUrl;

    public static ProfileDialogFragment newInstance(String userName, int age, String interests, String description, String profileImageUrl) {
        ProfileDialogFragment fragment = new ProfileDialogFragment();
        Bundle args = new Bundle();
        args.putString(userNameArg, userName);
        args.putInt(ageArg, age);
        args.putString(interestsArg, interests);
        args.putString(descriptionArg, description);
        args.putString(profileImageUrlArg, profileImageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           userName = getArguments().getString(userNameArg);
           age = getArguments().getInt(ageArg);
           interests = getArguments().getString(interestsArg);
           description = getArguments().getString(descriptionArg);
           profileImageUrl = getArguments().getString(profileImageUrlArg);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ProfileDialogFragmentStyle);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_user_expanded, container, false);
        TextView userNameTextView = view.findViewById(R.id.profileNameTV);
        TextView ageTextView = view.findViewById(R.id.profileAgeTV);
        TextView interestsTextView = view.findViewById(R.id.profileInterestsTV);
        TextView descriptionTextView = view.findViewById(R.id.profileDescriptionTV);
        ImageView profileImageView = view.findViewById(R.id.profileIV);

        userNameTextView.setText(userName);
        ageTextView.append(String.valueOf(age));
        interestsTextView.append(interests);
        descriptionTextView.setText(description);

        if (profileImageUrl != null) {
            Picasso.get()
                    .load(profileImageUrl)
                    .placeholder(R.drawable.nav_profile)
                    .into(profileImageView);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }
}