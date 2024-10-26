package com.danrat.tomocom.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.danrat.tomocom.Model.Chat;
import com.danrat.tomocom.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ChatRoomsViewModel extends ViewModel {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<Chat>> chatDataList = new MutableLiveData<>();
    private final List<String> usernames = new ArrayList<>();
    private final List<String> descriptions = new ArrayList<>();
    private final List<String> uidList = new ArrayList<>();
    private final List<String> profilePicturesUrls = new ArrayList<>();
    private final List<Date> dateList = new ArrayList<>();
    private final String userID;

    public ChatRoomsViewModel() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    public LiveData<List<Chat>> getChatRooms() { return chatDataList; }

    public List<String> getUsernames() { return usernames; }
    public List<String> getUidList() { return uidList; }
    public List<String> getProfilePicturesUrls() { return profilePicturesUrls; }
    public List<String> getDescriptions() { return descriptions; }
    public String getUserID() { return userID; }

    public void fetchChatData(List<String> friends, List<User> users) {
        if (friends.isEmpty())
            chatDataList.setValue(new ArrayList<>());
        else {
            firestore.collection("chat_rooms")
                    .whereArrayContainsAny("members", friends)
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            Log.w("Firestore", "Listen failed", e);
                            return;
                        }
                        if (snapshot != null && !snapshot.isEmpty()) {
                            List<Chat> chatRooms = new ArrayList<>();
                            for (DocumentSnapshot document : snapshot.getDocuments()) {
                                Chat chat = document.toObject(Chat.class);
                                if (chat != null && chat.getMembers().contains(userID))
                                    chatRooms.add(chat);
                            }

                            usernames.clear();
                            descriptions.clear();
                            profilePicturesUrls.clear();
                            uidList.clear();
                            dateList.clear();

                            for (Chat chat : chatRooms) {
                                List<String> tempMembers = new ArrayList<>(chat.getMembers());
                                if (tempMembers.contains(userID)) {
                                    tempMembers.remove(userID);
                                    for (User user : users) {
                                        if (Objects.equals(user.getUid(), tempMembers.get(0))) {
                                            usernames.add(user.getUsername());
                                            descriptions.add(user.getDescription());
                                            profilePicturesUrls.add(user.getProfileImageUrl());
                                            uidList.add(tempMembers.get(0));
                                            if (chat.getMessages().isEmpty()) {
                                                dateList.add(new Date());
                                            } else {
                                                dateList.add(chat.getLastMessage().getCreatedAt());
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                            sortChatsByDate(chatRooms, dateList, usernames, uidList, descriptions, profilePicturesUrls);

                            chatDataList.setValue(chatRooms);

                            /* for (DocumentChange dc : snapshot.getDocumentChanges()) {
                                switch (dc.getType()) {
                                    case ADDED:
                                        Log.d("TAG", "New Chat: " + dc.getDocument().toObject(Message.class));
                                        break;
                                    case MODIFIED:
                                        Log.d("TAG", "Modified Chat: " + dc.getDocument().toObject(Message.class));
                                        break;
                                    case REMOVED:
                                        Log.d("TAG", "Removed Chat: " + dc.getDocument().toObject(Message.class));
                                        break;
                                }
                            } */

                        } else {
                            chatDataList.setValue(new ArrayList<>());
                        }
                    });
        }
    }

    private void sortChatsByDate(List<Chat> chats, List<Date> dates, List<String> usernames, List<String> uidList, List<String> descriptions, List<String> profilePicturesUrls) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            indices.add(i);
        }

        indices.sort(Comparator.comparing(dates::get).reversed());

        List<Chat> sortedChats = new ArrayList<>();
        List<Date> sortedDates = new ArrayList<>();
        List<String> sortedUsernames = new ArrayList<>();
        List<String> sortedUidList = new ArrayList<>();
        List<String> sortedProfilePicturesUrls = new ArrayList<>();
        List<String> sortedDescriptions = new ArrayList<>();

        for (int index : indices) {
            sortedChats.add(chats.get(index));
            sortedDates.add(dates.get(index));
            sortedUsernames.add(usernames.get(index));
            sortedUidList.add(uidList.get(index));
            sortedProfilePicturesUrls.add(profilePicturesUrls.get(index));
            sortedDescriptions.add(descriptions.get(index));
        }

        chats.clear();
        chats.addAll(sortedChats);

        dates.clear();
        dates.addAll(sortedDates);

        usernames.clear();
        usernames.addAll(sortedUsernames);

        uidList.clear();
        uidList.addAll(sortedUidList);

        profilePicturesUrls.clear();
        profilePicturesUrls.addAll(sortedProfilePicturesUrls);

        descriptions.clear();
        descriptions.addAll(sortedDescriptions);
    }

    public void removeFriend(String uid, String cid) {
        DocumentReference docRef = firestore.collection("users").document(userID);

        docRef.update("friends", FieldValue.arrayRemove(uid))
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "User " + uid + "removed from friends.");
                    removeChat(cid);
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error on removing friend: ", e));
    }

    public void removeAndBlockFriend(String uid, String cid) {
        DocumentReference docRef = firestore.collection("users").document(userID);
        removeFriend(uid, cid);
        docRef.update("skipped", FieldValue.arrayUnion(uid))
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "User " + uid + "added to blocked.");
                    removeChat(cid);
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error adding to blocked: ", e));
    }

    public void removeChat (String cid) {
        DocumentReference docRef = firestore.collection("chat_rooms").document(cid);
        docRef.delete()
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Chat room" + cid + " removed."))
                .addOnFailureListener(e -> Log.w("Firestore", "Error removing chat room: ", e));
    }

}

