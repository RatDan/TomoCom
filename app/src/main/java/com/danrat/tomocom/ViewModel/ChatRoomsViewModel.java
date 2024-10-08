package com.danrat.tomocom.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.danrat.tomocom.Model.Chat;
import com.danrat.tomocom.Model.Message;
import com.danrat.tomocom.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private final List<String> uidList = new ArrayList<>();
    private final List<String> profilePicturesUrls = new ArrayList<>();
    private final List<Chat> chatList = new ArrayList<>();
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
    public List<Chat> getChatList() { return chatList; }

    public void fetchChatData(List<String> friends) {
        if (friends.isEmpty()) return;

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
                            chatRooms.add(chat);
                        }
                        chatDataList.setValue(chatRooms);

                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
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
                        }
                    } else {
                        chatDataList.setValue(new ArrayList<>());
                    }
                });
    }

    public void prepareChatData(List<Chat> chats, List<User> users) {
        chatList.clear();
        usernames.clear();
        uidList.clear();
        profilePicturesUrls.clear();
        dateList.clear();

        for (Chat chat : chats) {
            List<String> tempMembers = new ArrayList<>(chat.getMembers());
            if (tempMembers.contains(userID)) {
                tempMembers.remove(userID);
                for (User user : users) {
                    if (Objects.equals(user.getUid(), tempMembers.get(0))) {
                        chatList.add(chat);
                        usernames.add(user.getUsername());
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
        sortChatsByDate(chatList,dateList,usernames,uidList,profilePicturesUrls);
    }

    private void sortChatsByDate(List<Chat> chats, List<Date> dates, List<String> usernames, List<String> uidList, List<String> profilePicturesUrls) {
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

        for (int index : indices) {
            sortedChats.add(chats.get(index));
            sortedDates.add(dates.get(index));
            sortedUsernames.add(usernames.get(index));
            sortedUidList.add(uidList.get(index));
            sortedProfilePicturesUrls.add(profilePicturesUrls.get(index));
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
    }

    public void onRemoveFriend() {

    }

}

