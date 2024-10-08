package com.danrat.tomocom.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danrat.tomocom.Model.Chat;
import com.danrat.tomocom.Model.Message;
import com.danrat.tomocom.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private OnMenuItemClickListener menuItemClickListener;
    private List<Chat> chatList;
    private List<String> usernameList;
    private List<String> uidList;
    private List<String> profilePictureList;
    private OnItemClickListener listener;

    public FriendListAdapter(List<Chat> chatList, List<String> usernameList, List<String> uidList, List<String> profilePictureList, OnMenuItemClickListener menuItemClickListener) {
        this.chatList = chatList;
        this.usernameList = usernameList;
        this.uidList = uidList;
        this.profilePictureList = profilePictureList;
        this.menuItemClickListener = menuItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(List<Message> messages, String username, String uid, String cid, String profilePictureUrl);
    }

    public interface OnMenuItemClickListener {
        void onRemoveFriend();
        void onRemoveAndBlockFriend();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_friend, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.clear();
        Chat chat = chatList.get(position);
        String username = usernameList.get(position);
        String uid = uidList.get(position);
        String profilePictureUrl = profilePictureList.get(position);
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Picasso.get().load(profilePictureUrl).placeholder(R.drawable.nav_profile).into(holder.profileImageView);
        } else {
            holder.profileImageView.setImageResource(R.drawable.nav_profile);
        }
        holder.bind(chat, username, profilePictureUrl);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(chat.getMessages(), username, uid, chat.getCid(), profilePictureUrl);
                }
            }
        });

        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.more_options_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.remove_friend:
                                menuItemClickListener.onRemoveFriend();
                                return true;
                            case R.id.remove_and_block_friend:
                                menuItemClickListener.onRemoveAndBlockFriend();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });
    }



    @Override
    public int getItemCount() { return chatList.size(); }

    public static String formatDate(Date date) {
        return DateFormat.getDateTimeInstance().format(date);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Chat> newChatRooms, List<String> newUsernames, List<String> newUids, List<String> newProfilePictureList) {
        this.chatList = newChatRooms;
        this.usernameList = newUsernames;
        this.uidList = newUids;
        this.profilePictureList = newProfilePictureList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView textViewName ;
        private final TextView textViewMessage;
        private final TextView textViewDate;
        private final ImageView profileImageView;
        private final ImageButton moreButton;

        public ViewHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.TV1);
            textViewMessage = itemView.findViewById(R.id.TV2);
            textViewDate = itemView.findViewById(R.id.TV3);
            profileImageView = itemView.findViewById(R.id.profileIV);
            moreButton = itemView.findViewById(R.id.moreButton);
        }

        public void clear() {
            textViewName.setText("");
            textViewMessage.setText(R.string.placeholder_message);
            textViewDate.setText(R.string.card_time);
        }

        public void bind(Chat chat, String username, String profileImageUrl) {
            textViewName.setText(username);
            textViewMessage.setText(chat.getLastMessage().getMessage());
            textViewDate.setText(formatDate(chat.getLastMessage().getCreatedAt()));
        }

    }
}
