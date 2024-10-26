package com.danrat.tomocom.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danrat.tomocom.Adapter.FriendListAdapter;
import com.danrat.tomocom.R;
import com.danrat.tomocom.ViewModel.ChatRoomsViewModel;

import com.danrat.tomocom.ViewModel.UserListViewModel;

public class FriendsFragment extends Fragment implements FriendListAdapter.OnMenuItemClickListener {

    private RecyclerView recyclerView;
    private FriendListAdapter adapter;
    private TextView emptyRecyclerTextView;
    private ImageView friendsIconImageView;
    private ChatRoomsViewModel chatRoomsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        recyclerView = view.findViewById(R.id.friendsRV);
        emptyRecyclerTextView = view.findViewById(R.id.emptyRecyclerTV);
        friendsIconImageView = view.findViewById(R.id.friendsIconIV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemViewCacheSize(20);

        UserListViewModel userListViewModel = new ViewModelProvider(requireActivity()).get(UserListViewModel.class);
        chatRoomsViewModel = new ViewModelProvider(requireActivity()).get(ChatRoomsViewModel.class);

        userListViewModel.getUserList().observe(getViewLifecycleOwner(), users -> {
            if (users != null) {
                userListViewModel.filterFriends(users);
                chatRoomsViewModel.fetchChatData(userListViewModel.getFriendsUids(), userListViewModel.getFriends());
            }
        });

        chatRoomsViewModel.getChatRooms().observe(getViewLifecycleOwner(), chats -> {
            if (chats != null) {
                if (adapter == null) {
                    adapter = new FriendListAdapter(
                            chats,
                            chatRoomsViewModel.getUsernames(),
                            chatRoomsViewModel.getUidList(),
                            chatRoomsViewModel.getDescriptions(),
                            chatRoomsViewModel.getProfilePicturesUrls(),
                            chatRoomsViewModel.getUserID(),
                            this
                    );

                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener((username, uid, cid, description, profilePictureUrl) -> {
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("uid", uid);
                        intent.putExtra("cid", cid);
                        intent.putExtra("profilePictureUrl", profilePictureUrl);
                        intent.putExtra("description", description);
                        startActivity(intent);
                    });
                } else {
                    adapter.updateData(
                            chats,
                            chatRoomsViewModel.getUsernames(),
                            chatRoomsViewModel.getUidList(),
                            chatRoomsViewModel.getDescriptions(),
                            chatRoomsViewModel.getProfilePicturesUrls(),
                            chatRoomsViewModel.getUserID(),
                            this
                    );
                }
            }

            checkIfEmpty();
        });

        return view;
    }

    @Override
    public void onRemoveFriend (String uid, String cid) { chatRoomsViewModel.removeFriend(uid, cid); }

    @Override
    public void onRemoveAndBlockFriend (String uid, String cid) { chatRoomsViewModel.removeAndBlockFriend(uid, cid); }

    private void checkIfEmpty() {
        if (adapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyRecyclerTextView.setVisibility(View.VISIBLE);
            friendsIconImageView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyRecyclerTextView.setVisibility(View.GONE);
            friendsIconImageView.setVisibility(View.GONE);
        }
    }
}
