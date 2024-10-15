package com.danrat.tomocom.ViewModel;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.danrat.tomocom.Model.Chat;
import com.danrat.tomocom.Model.Interests;
import com.danrat.tomocom.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UserListViewModel extends ViewModel {

    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<User>> userList = new MutableLiveData<>();
    private final MutableLiveData<List<User>> filteredUserList = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> matchLevels = new MutableLiveData<>();
    private final MutableLiveData<List<String>> profileImageUrls = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private List<Interests> currentUserInterests = new ArrayList<>();
    private final List<User> friends = new ArrayList<>();
    private final List<String> friendsUids = new ArrayList<>();
    private final String userID;
    private List<String> friendList = new ArrayList<>();
    private List<String> skippedList = new ArrayList<>();

    public UserListViewModel() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    public LiveData<List<User>> getUserList() {
        return userList;
    }

    public LiveData<List<User>> getFilteredUserList() {
        return filteredUserList;
    }

    public LiveData<List<Integer>> getMatchLevels() {
        return matchLevels;
    }

    public LiveData<List<String>> getProfileImageUrls() { return profileImageUrls; }

    public List<User> getFriends() { return friends; }

    public List<String> getFriendsUids() { return friendsUids; }

    public LiveData<String> getToastMessage() { return toastMessage; }

    public void fetchUsersData(int minMatch, int minAge) {
        fireStore.collection("users")
                .limit(10)
                .addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w("UserListViewModel", "Listen failed.", e);
                return;
            }

            if (snapshot != null && !snapshot.isEmpty()) {
                List<User> users = new ArrayList<>();
                for (DocumentSnapshot doc : snapshot.getDocuments()) {
                    User user = doc.toObject(User.class);
                    if (user != null) {
                        users.add(user);
                    }
                }
                filterAndFetchCurrentUser(users, minMatch, minAge);
                userList.setValue(users);
            }
        });
    }

    private void filterAndFetchCurrentUser(List<User> users, int minMatch, int minAge) {
        DocumentReference currentUserRef = fireStore.collection("users").document(userID);
        currentUserRef.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.w("UserListViewModel", "Listen failed.", e);
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                User currentUser = documentSnapshot.toObject(User.class);
                if (currentUser != null) {
                    currentUserInterests = currentUser.getInterests() != null ? currentUser.getInterests() : new ArrayList<>();
                    friendList = currentUser.getFriends() != null ? currentUser.getFriends() : new ArrayList<>();
                    skippedList = currentUser.getSkipped() != null ? currentUser.getSkipped() : new ArrayList<>();

                    List<User> filteredUsers = new ArrayList<>();
                    for (User user : users) {
                        if (!user.getUid().equals(userID) &&
                                !friendList.contains(user.getUid()) &&
                                !skippedList.contains(user.getUid()) &&
                                user.getAge() >= minAge) {
                            filteredUsers.add(user);
                        }
                    }

                    List<Integer> matchLevelList = calculateMatchLevelsForUsers(filteredUsers);
                    Iterator<Integer> mIterator = matchLevelList.iterator();
                    Iterator<User> uIterator = filteredUsers.iterator();

                    while (mIterator.hasNext() && uIterator.hasNext()) {
                        Integer matchLevel = mIterator.next();
                        User filteredUser = uIterator.next();
                        if (matchLevel < minMatch) {
                            mIterator.remove();
                            uIterator.remove();
                        }

                    }

                    sortUsersByMatchLevel(filteredUsers, matchLevelList);

                    filteredUserList.setValue(filteredUsers);
                    matchLevels.setValue(matchLevelList);

                    loadProfileImagesForUsers(filteredUsers);
                }
            }
        });
    }

    private List<Integer> calculateMatchLevelsForUsers(List<User> users) {
        List<Integer> calculatedMatchLevels = new ArrayList<>();
        for (User user : users) {
            calculatedMatchLevels.add(calculateMatchLevel(currentUserInterests, user.getInterests()));
        }
        return calculatedMatchLevels;
    }

    private int calculateMatchLevel(List<Interests> currentUserInterests, List<Interests> otherUserInterests) {
        int matchLevel = 0;
        Set<Interests> matchedInterestsSet = new HashSet<>(otherUserInterests);
        for (Interests interest : currentUserInterests) {
            if (matchedInterestsSet.contains(interest)) {
                matchLevel += 20;
            }
        }
        return matchLevel;
    }

    public void loadProfileImagesForUsers(List<User> users) {
        List<String> imageUrls = new ArrayList<>();

        for (User user : users) {
            imageUrls.add(user.getProfileImageUrl());
        }
        profileImageUrls.setValue(imageUrls);
    }

    public void addUserToFriends(User user, String uid, String username) {
        DocumentReference currentUserRef = fireStore.collection("users").document(userID);
        CollectionReference chatReference = fireStore.collection("chat_rooms");
        currentUserRef.update("friends", FieldValue.arrayUnion(uid))
                .addOnSuccessListener(aVoid -> {
                    friendList.add(uid);
                    if (user.getFriends().contains(userID) && friendList.contains(user.getUid())) {
                        List<String> members = Arrays.asList(userID,uid);
                        DocumentReference newChatRef = chatReference.document();
                        String cId = newChatRef.getId();
                        Chat tempChat = new Chat(cId, members);
                        newChatRef
                                .set(tempChat)
                                .addOnSuccessListener(bVoid -> {
                                    Log.d(TAG, "Chat room added with ID: " + cId);
                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, "Error adding chat document", e);
                                });
                    }
                    toastMessage.setValue("Użytkownik " + username + " został dodany do znajomych!");
                })
                .addOnFailureListener(e -> Log.w("UserListViewModel", "Error adding user to friends", e));

    }

    public void skipUser(String uid, String username) {
        DocumentReference currentUserRef = fireStore.collection("users").document(userID);
        currentUserRef.update("skipped", FieldValue.arrayUnion(uid))
                .addOnSuccessListener(aVoid -> {
                    toastMessage.setValue("Użytkownik " + username + " został pominięty!");
                })
                .addOnFailureListener(e -> Log.w("UserListViewModel", "Error skipping user", e));
    }

    public void filterFriends(List<User> users) {
        friends.clear();
        friendsUids.clear();
        for (User user : users) {
            if (!Objects.equals(user.getUid(), userID) && user.getFriends().contains(userID) && friendList.contains(user.getUid())) {
                friends.add(user);
                friendsUids.add(user.getUid());
            }
        }
    }

    private void sortUsersByMatchLevel(List<User> users, List<Integer> matches) {
        List<Pair<User, Integer>> userMatchPairs = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            userMatchPairs.add(new Pair<>(users.get(i), matches.get(i)));
        }

        userMatchPairs.sort((pair1, pair2) -> pair2.second - pair1.second);

        users.clear();
        matches.clear();
        for (Pair<User, Integer> pair : userMatchPairs) {
            users.add(pair.first);
            matches.add(pair.second);
        }
    }
}
