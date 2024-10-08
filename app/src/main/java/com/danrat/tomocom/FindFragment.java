package com.danrat.tomocom;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.danrat.tomocom.Adapter.UserListAdapter;
import com.danrat.tomocom.Model.Chat;
import com.danrat.tomocom.Model.Interests;
import com.danrat.tomocom.Model.User;
import com.danrat.tomocom.ViewModel.ProfilePictureViewModel;
import com.danrat.tomocom.ViewModel.UserListViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FindFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);

        recyclerView = view.findViewById(R.id.matchedRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        UserListViewModel userListViewModel = new ViewModelProvider(requireActivity()).get(UserListViewModel.class);

        userListViewModel.getFilteredUserList().observe(getViewLifecycleOwner(), userList -> {
            if (adapter == null) {
                adapter = new UserListAdapter(userList, new ArrayList<>(), new ArrayList<>());
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(User user) {
                        ProfileDialogFragment dialogFragment = ProfileDialogFragment.newInstance(user.getUsername(), user.getAge(), user.getInterestsString(), user.getDescription(), user.getProfileImageUrl());
                        dialogFragment.show(getParentFragmentManager(), "ProfileDialogFragment");
                    }

                    @Override
                    public void onAddClick(User user, int position) {
                        userListViewModel.addUserToFriends(user, user.getUid(), user.getUsername());
                    }

                    @Override
                    public void onSkipClick(User user, int position) {
                        userListViewModel.skipUser(user.getUid(), user.getUid());
                    }
                });
            } else {
                adapter.updateData(userList, new ArrayList<>(), new ArrayList<>());

            }

        });

        userListViewModel.getProfileImageUrls().observe(getViewLifecycleOwner(), imageUrls -> {
            if (adapter != null) {
                adapter.updateProfileImages(imageUrls);
            }
        });

        userListViewModel.getMatchLevels().observe(getViewLifecycleOwner(), matchLevels -> {
            if (adapter != null) {
                adapter.updateMatchLevels(matchLevels);
            }
        });

        userListViewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        userListViewModel.fetchUsersData();

        return view;
    }


}