package com.danrat.tomocom;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DocumentReference documentReference = fireStore.collection("users").document(userID);
        CollectionReference chatReference = fireStore.collection("chat_rooms");

        matchLevelList = new ArrayList<>();
        interestList = new ArrayList<>();
        friendList = new ArrayList<>();
        skippedList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.matchedRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        UserListViewModel userListViewModel = new ViewModelProvider(requireActivity()).get(UserListViewModel.class);
        userListViewModel.getUserList().observe(getViewLifecycleOwner(), userList -> {
            users = new ArrayList<>(userList);
            matchLevelList.clear();

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

            FragmentUtils.sortLists(matchLevelList, users, Comparator.reverseOrder());

            if (adapter == null) {
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
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(getActivity(), "Użytkownik " + username + " został dodany do znajomych!", Toast.LENGTH_SHORT).show();
                                    friendList.add(uid);
                                    for (User user : userList) {
                                        if (user.getFriends().contains(userID) && friendList.contains(user.getUid()) && Objects.equals(uid, user.getUid())) {
                                            List<String> members = Arrays.asList(userID,uid);
                                            DocumentReference newChatRef = chatReference.document();
                                            String cId = newChatRef.getId();
                                            Chat tempChat = new Chat(cId, members);
                                            /* fireStore.collection("chat_rooms")
                                                    .add(tempChat)
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
                                                    }); */
                                            newChatRef
                                                    .set(tempChat)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Log.d(TAG, "Chat room added with ID: " + cId);
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.w(TAG, "Error adding chat document", e);
                                                    });
                                        }
                                    }
                                    adapter.removeItem(position);
                                })
                                .addOnFailureListener(e -> Log.w(TAG, "Error adding user to friends", e));
                    }

                    @Override
                    public void onSkipClick(String uid, String username, int position) {
                        documentReference
                                .update("skipped", FieldValue.arrayUnion(uid))
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(getActivity(), "Użytkownik " + username + " został pominięty!", Toast.LENGTH_SHORT).show();
                                    adapter.removeItem(position);
                                })
                                .addOnFailureListener(e -> Log.w(TAG, "Error skipping user", e));
                    }
                });
            } else {
                adapter.updateData(users, matchLevelList);
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