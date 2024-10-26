package com.danrat.tomocom.View;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.danrat.tomocom.Adapter.UserListAdapter;
import com.danrat.tomocom.Model.User;
import com.danrat.tomocom.R;
import com.danrat.tomocom.ViewModel.UserListViewModel;

import java.util.ArrayList;
import java.util.Objects;


public class FindFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView recyclerView;
    private TextView emptyRecyclerTextView;
    private ImageView searchIconImageView;
    private UserListAdapter adapter;
    private UserListViewModel userListViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);

        recyclerView = view.findViewById(R.id.matchedRV);
        emptyRecyclerTextView = view.findViewById(R.id.emptyRecyclerTV);
        searchIconImageView = view.findViewById(R.id.searchIconIV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemViewCacheSize(20);

        userListViewModel = new ViewModelProvider(requireActivity()).get(UserListViewModel.class);

        userListViewModel.getFilteredUserList().observe(getViewLifecycleOwner(), userList -> {
            if (adapter == null) {
                adapter = new UserListAdapter(userList, new ArrayList<>(), new ArrayList<>());
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(User user) {
                        ProfileDialogFragment dialogFragment = ProfileDialogFragment.newInstance(user.getUsername(), user.getAge(), user.getInterestsString(), user.getDescription(), user.getProfileImageUrl(), user.getUid(), user.getFriends());
                        dialogFragment.show(getParentFragmentManager(), "ProfileDialogFragment");
                    }

                    @Override
                    public void onAddClick(User user, int position) {
                        userListViewModel.addUserToFriends(user, user.getUid(), user.getUsername());
                    }

                    @Override
                    public void onSkipClick(User user, int position) {
                        userListViewModel.skipUser(user.getUid(), user.getUsername());
                    }
                });
            } else {
                adapter.updateData(userList, new ArrayList<>(), new ArrayList<>());

            }

            checkIfEmpty();
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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        int minMatchValue = Integer.parseInt(sharedPreferences.getString("match","40"));
        int minAgeValue = Integer.parseInt(sharedPreferences.getString("age","13"));
        userListViewModel.fetchUsersData(minMatchValue, minAgeValue);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
        if (Objects.equals(key, "mode"))
            return;

        int minMatch = Integer.parseInt(sharedPreferences.getString("match", "40"));
        int minAge = Integer.parseInt(sharedPreferences.getString("age","13"));
        if (key != null) {
            if (key.equals("match")) {
                minMatch = Integer.parseInt(sharedPreferences.getString(key, "40"));
            }
            if (key.equals("age")) {
                minAge = Integer.parseInt(sharedPreferences.getString(key, "13"));
            }
        }
        userListViewModel.fetchUsersData(minMatch, minAge);
    }

    private void checkIfEmpty() {
        if (adapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyRecyclerTextView.setVisibility(View.VISIBLE);
            searchIconImageView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyRecyclerTextView.setVisibility(View.GONE);
            searchIconImageView.setVisibility(View.GONE);
        }
    }
}