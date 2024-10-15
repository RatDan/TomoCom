package com.danrat.tomocom.View;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.danrat.tomocom.Model.User;
import com.danrat.tomocom.R;
import com.danrat.tomocom.ViewModel.UserListViewModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class ProfileDialogFragment extends DialogFragment {

    private static final String userNameArg = "username";
    private static final String ageArg = "age";
    private static final String interestsArg = "interests";
    private static final String descriptionArg = "description";
    private static final String profileImageUrlArg = "profileImageUrl";
    private static final String uidArg = "uid";
    private static final String friendsArg = "friends";

    private String userName;
    private int age;
    private String interests;
    private String description;
    private String profileImageUrl;
    private String uid;
    private List<String> friends;

    public static ProfileDialogFragment newInstance(String userName, int age, String interests, String description, String profileImageUrl, String uid, List<String> friends) {
        ProfileDialogFragment fragment = new ProfileDialogFragment();
        Bundle args = new Bundle();
        args.putString(userNameArg, userName);
        args.putInt(ageArg, age);
        args.putString(interestsArg, interests);
        args.putString(descriptionArg, description);
        args.putString(profileImageUrlArg, profileImageUrl);
        args.putString(uidArg, uid);
        args.putSerializable(friendsArg, (Serializable) friends);
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
           uid = getArguments().getString(uidArg);
           friends = (List<String>) getArguments().getSerializable(friendsArg);
        }
        //setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ProfileDialogFragmentStyle);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UserListViewModel userListViewModel = new ViewModelProvider(requireActivity()).get(UserListViewModel.class);

        View view = inflater.inflate(R.layout.card_user_expanded, container, false);
        TextView userNameTextView = view.findViewById(R.id.profileNameTV);
        TextView ageTextView = view.findViewById(R.id.profileAgeTV);
        TextView interestsTextView = view.findViewById(R.id.profileInterestsTV);
        TextView descriptionTextView = view.findViewById(R.id.profileDescriptionTV);
        ImageView profileImageView = view.findViewById(R.id.profileIV);
        Button addButton = view.findViewById(R.id.addFriendButton);
        Button skipButton = view.findViewById(R.id.skipButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userListViewModel.addUserToFriends(new User(uid, friends), uid, userName);
                dismiss();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userListViewModel.skipUser(uid,userName);
                dismiss();
            }
        });


        userNameTextView.setText(userName);
        ageTextView.append(" " + String.valueOf(age));
        interestsTextView.append(" " + String.valueOf(interests));
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