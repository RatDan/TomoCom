package com.danrat.tomocom.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danrat.tomocom.Model.Chat;
import com.danrat.tomocom.Model.Message;
import com.danrat.tomocom.Model.User;
import com.danrat.tomocom.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private List<Chat> chatList;
    private List<String> usernameList;
    private List<String> uidList;
    private OnItemClickListener listener;

    public FriendListAdapter(List<Chat> chatList, List<String> usernameList, List<String> uidList) {
        this.chatList = chatList;
        this.usernameList = usernameList;
        this.uidList = uidList;
    }

    public interface OnItemClickListener {
        void onItemClick(List<Message> messages, String username, String uid);
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
        holder.bind(chat, username);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(chat.getMessages(), username, uid);
                }
            }
        });
    }

    @Override
    public int getItemCount() { return chatList.size(); }

    public void removeItem (int position) {
        chatList.remove(position);
        usernameList.remove(position);
        uidList.remove(position);
        notifyItemRemoved(position);
    }

    public static String formatDate(Date date)
    {
        return DateFormat.getDateTimeInstance().format(date);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Chat> newChatRooms, List<String> newUsernames, List<String> newUids) {
        this.chatList = newChatRooms;
        this.usernameList = newUsernames;
        this.uidList = newUids;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView textViewName ;
        private final TextView textViewMessage;
        private final TextView textViewDate;

        public ViewHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.TV1);
            textViewMessage = itemView.findViewById(R.id.TV2);
            textViewDate = itemView.findViewById(R.id.TV3);
        }

        public void clear() {
            textViewName.setText("");
            textViewMessage.setText(R.string.placeholder_message);
            textViewDate.setText(R.string.card_time);
        }

        public void bind(Chat chat, String username) {
            textViewName.setText(username);
            textViewMessage.setText(chat.getLastMessage().getMessage());
            textViewDate.setText(formatDate(chat.getLastMessage().getCreatedAt()));
        }

    }
}
