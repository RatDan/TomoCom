package com.danrat.tomocom;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danrat.tomocom.Adapter.FriendListAdapter;
import com.danrat.tomocom.Model.Chat;
import com.danrat.tomocom.Model.ChatRoomsViewModel;
import com.danrat.tomocom.Model.Message;
import com.danrat.tomocom.Model.User;
import com.danrat.tomocom.Model.UserListViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FriendsFragment extends Fragment {

    private List<Chat> chatList;
    private List<String> usernames;
    private List<String> uidList;
    private List<String> friends;
    private List<Date> dateList;
    private List<User> users;
    private List<Message> messages;
    private List<String> tempMembers;
    private RecyclerView recyclerView;
    private FriendListAdapter adapter;
    private ChatRoomsViewModel chatRoomsViewModel;
    private UserListViewModel userListViewModel;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        chatList = new ArrayList<>();
        usernames = new ArrayList<>();
        friends = new ArrayList<>();
        uidList = new ArrayList<>();
        dateList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.friendsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userListViewModel = new ViewModelProvider(requireActivity()).get(UserListViewModel.class);
        userListViewModel.getUserList().observe(getViewLifecycleOwner(), userList -> {
            if (userList != null) {
                users = new ArrayList<>(userList);
                for (User user : users) {
                    if (Objects.equals(user.getUid(), userID)) {
                        friends = user.getFriends();
                    }
                }
                users.removeIf(user -> Objects.equals(user.getUid(), userID));
                chatRoomsViewModel.fetchChatData(friends);
            }
        });

        chatRoomsViewModel = new ViewModelProvider(requireActivity()).get(ChatRoomsViewModel.class);
        chatRoomsViewModel.getChatRooms().observe(getViewLifecycleOwner(), chats -> {
            if (chats != null) {
                chatList = new ArrayList<>(chats);
                usernames.clear();
                uidList.clear();
                dateList.clear();

                 for (Chat chat : chatList) {
                    tempMembers = chat.getMembers();
                    if (tempMembers.contains(userID)) {
                        tempMembers.remove(userID);
                        for (User user : users) {
                            if (Objects.equals(user.getUid(), tempMembers.get(0)) && usernames.size() != chatList.size()) {
                                usernames.add(user.getUsername());
                            }
                        }
                        uidList.add(tempMembers.get(0));
                        messages = chat.getMessages();
                        if (messages.isEmpty()) {
                            dateList.add(new Date());
                        } else {
                            for (Message message : messages) {
                                dateList.add(message.getCreatedAt());
                            }
                        }
                    }
                }

                //FragmentUtils.sortLists(dateList, chatList, Comparator.naturalOrder());

                if (adapter == null) {
                    adapter = new FriendListAdapter(chatList, usernames, uidList);
                    recyclerView.setAdapter(adapter);

                    /* adapter.setOnItemClickListener(new FriendListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(List<Message> messages, String username, String uid) {
                            ChatFragment fragment = ChatFragment.newInstance(messages, username, uid);
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentUtils.replaceFragment(fragmentManager, fragment);
                        }
                    }); */

                    adapter.setOnItemClickListener(new FriendListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(List<Message> messages, String username, String uid, String cid) {
                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("uid", uid);
                            intent.putExtra("cid",cid);
                            startActivity(intent);
                        }
                    });
                } else {
                    adapter.updateData(chatList, usernames, uidList);
                }
            }
        });

        return view;
    }
}