package com.danrat.tomocom;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danrat.tomocom.Adapter.FriendListAdapter;
import com.danrat.tomocom.Adapter.UserListAdapter;
import com.danrat.tomocom.Model.Chat;
import com.danrat.tomocom.ViewModel.ChatRoomsViewModel;
import com.danrat.tomocom.Model.Message;

import com.danrat.tomocom.ViewModel.UserListViewModel;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment implements FriendListAdapter.OnMenuItemClickListener {

    private RecyclerView recyclerView;
    private FriendListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        recyclerView = view.findViewById(R.id.friendsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        UserListViewModel userListViewModel = new ViewModelProvider(requireActivity()).get(UserListViewModel.class);
        ChatRoomsViewModel chatRoomsViewModel = new ViewModelProvider(requireActivity()).get(ChatRoomsViewModel.class);

        userListViewModel.getUserList().observe(getViewLifecycleOwner(), users -> {
            if (users != null) {
                userListViewModel.filterFriends(users);
                chatRoomsViewModel.fetchChatData(userListViewModel.getFriendsUids());
            }
        });

        chatRoomsViewModel.getChatRooms().observe(getViewLifecycleOwner(), chats -> {
            if (chats != null) {
                chatRoomsViewModel.prepareChatData(chats, userListViewModel.getFriends());

                if (adapter == null) {
                    adapter = new FriendListAdapter(
                            chatRoomsViewModel.getChatList(),
                            chatRoomsViewModel.getUsernames(),
                            chatRoomsViewModel.getUidList(),
                            chatRoomsViewModel.getProfilePicturesUrls(),
                            this
                    );

                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new FriendListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(List<Message> messages, String username, String uid, String cid, String profilePictureUrl) {
                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("uid", uid);
                            intent.putExtra("cid", cid);
                            intent.putExtra("profilePictureUrl", profilePictureUrl);
                            startActivity(intent);
                        }
                    });
                } else {
                    adapter.updateData(
                            chatRoomsViewModel.getChatList(),
                            chatRoomsViewModel.getUsernames(),
                            chatRoomsViewModel.getUidList(),
                            chatRoomsViewModel.getProfilePicturesUrls()
                    );
                }
            }
        });

        public void onRemoveFriend() {
            chatRoomsViewModel.
        }

        return view;
    }
}
