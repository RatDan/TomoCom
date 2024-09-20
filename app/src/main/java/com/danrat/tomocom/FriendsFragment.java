package com.danrat.tomocom;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danrat.tomocom.Adapter.FriendListAdapter;
import com.danrat.tomocom.Adapter.UserListAdapter;
import com.danrat.tomocom.Model.Chat;
import com.danrat.tomocom.Model.ChatRoomsViewModel;
import com.danrat.tomocom.Model.Message;
import com.danrat.tomocom.Model.User;
import com.danrat.tomocom.Model.UserListViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FriendsFragment extends Fragment {

    private List<Chat> chatList;
    private List<String> usernames;
    private List<String> uids;
    private List<String> friends;
    private List<Date> dateList;
    private List<User> users;
    private List<Message> messages;
    private RecyclerView recyclerView;
    private FriendListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DocumentReference documentReference = fireStore.collection("users").document(userID);

        // Pokazanie BottomNavigation po wyjściu z chatu
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation);
        bottomNavigationView.setVisibility(View.VISIBLE);

        chatList = new ArrayList<>();
        usernames = new ArrayList<>();
        friends = new ArrayList<>();
        uids = new ArrayList<>();
        dateList = new ArrayList<>();

        UserListViewModel userListViewModel = new ViewModelProvider(requireActivity()).get(UserListViewModel.class);
        ChatRoomsViewModel chatRoomsViewModel = new ViewModelProvider(requireActivity()).get(ChatRoomsViewModel.class);
        userListViewModel.getUserList().observe(getViewLifecycleOwner(), userList -> {
            if (userList != null && adapter == null) {
                users = new ArrayList<>(userList);
                for (User user : users) {
                    if (Objects.equals(user.getUid(), userID))
                        friends = user.getFriends();
                }
                users.removeIf(user -> Objects.equals(user.getUid(), userID));
                for (User user : users) {
                    usernames.add(user.getUsername());
                    uids.add(user.getUid());;
                }
                chatRoomsViewModel.fetchChatData(friends);
            }
        });

        chatRoomsViewModel.getChatRooms().observe(getViewLifecycleOwner(), chats -> {
            if (chats != null) {
                for (Chat chat : chats) {
                    if (chat.getMembers().contains(userID)) {
                        chatList.add(chat);
                        messages = chat.getMessages();
                        if (messages.isEmpty())
                            dateList.add(new Date());
                        else
                            for (Message message : messages) dateList.add(message.getCreatedAt());
                    }
                }
                recyclerView = view.findViewById(R.id.friendsRV);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                FragmentUtils.sortLists(dateList,chatList, Comparator.naturalOrder());
                adapter = new FriendListAdapter(chatList, usernames);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new FriendListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(List<Message> messages) {
                        ChatFragment fragment = ChatFragment.newInstance(messages);
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentUtils.replaceFragment(fragmentManager, fragment);
                    }
                });
            }
        });

        userListViewModel.fetchUserData();

        return view;
    }
}