package com.danrat.tomocom;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.danrat.tomocom.Model.UserListViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

    private List<Integer> matchLevelList;
    private List<Interests> interestList;
    private List<String> friendList;
    private List<String> skippedList;
    private List<User> users;
    private RecyclerView recyclerView;
    private UserListAdapter adapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DocumentReference documentReference = fireStore.collection("users").document(userID);

        matchLevelList = new ArrayList<>();
        interestList = new ArrayList<>();
        friendList = new ArrayList<>();
        skippedList = new ArrayList<>();

        UserListViewModel userListViewModel = new ViewModelProvider(requireActivity()).get(UserListViewModel.class);
        userListViewModel.getUserList().observe(getViewLifecycleOwner(), userList -> {
            if (adapter == null) {
                users = new ArrayList<>(userList);
                for (User user : users) {
                    if (Objects.equals(user.getUid(), userID)) {
                        interestList = user.getInterests();
                        friendList = user.getFriends();
                        skippedList = user.getSkipped();
                    }
                }
                users.removeIf(user -> Objects.equals(user.getUid(), userID));
                users.removeIf(user -> friendList.contains(user.getUid()) || skippedList.contains(user.getUid()));
                for (User user : users) {
                    matchLevelList.add(calculateMatchLevel(interestList, user.getInterests()));
                }
                FragmentUtils.sortLists(matchLevelList,users,Comparator.reverseOrder());
                recyclerView = view.findViewById(R.id.matchedRV);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new UserListAdapter(users, matchLevelList);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(String userName, int age, String interests, String description) {
                        ProfileDialogFragment dialogFragment = ProfileDialogFragment.newInstance(userName, age, interests, description);
                        dialogFragment.show(getParentFragmentManager(), "ProfileDialogFragment");
                    }

                    @Override
                    public void onAddClick(String uid, String username, int position) {
                        documentReference
                                .update("friends", FieldValue.arrayUnion(uid))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG,"onSuccess: Document "+ userID +" friends updated successfully");
                                        Toast.makeText(getActivity(), "Użytkownik " + username + " został dodany do znajomych!",Toast.LENGTH_SHORT).show();
                                        for (User user : users) {
                                            if (user.getFriends().contains(userID) && friendList.contains(user.getUid()) && Objects.equals(uid, user.getUid())) {
                                                List<String> members = Arrays.asList(userID,uid);
                                                fireStore.collection("chat_rooms")
                                                        .add(new Chat(members))
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Log.d(TAG, "Chat room added with ID: " + documentReference.getId());
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error adding chat document", e);
                                                            }
                                                        });
                                            }
                                        }
                                        adapter.removeItem(position);
                                    }
                                });
                    }

                    @Override
                    public void onSkipClick(String uid, String username, int position) {
                        documentReference
                                .update("skipped",FieldValue.arrayUnion(uid))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG,"onSuccess: Document "+ userID +" skipped updated successfully");
                                        Toast.makeText(getActivity(), "Użytkownik " + username + " został pominięty!",Toast.LENGTH_SHORT).show();
                                        adapter.removeItem(position);
                                    }
                                });
                    }
                });
            }
        });

        userListViewModel.fetchUserData();

        return view;
    }

    private int calculateMatchLevel(List<Interests> interests, List<Interests> matchedInterests) {
        int matchLevel = 0;
        Set<Interests> matchedInterestsSet = new HashSet<>(matchedInterests);
        for (Interests interest : interests) {
            if (matchedInterestsSet.contains(interest)) {
                matchLevel += 20;
            }
        }
        return matchLevel;
    }
}