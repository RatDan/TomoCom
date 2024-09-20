package com.danrat.tomocom.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danrat.tomocom.Model.Chat;
import com.danrat.tomocom.Model.Message;
import com.danrat.tomocom.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private final List<Chat> chatList;
    private final List<String> usernameList;
    private OnItemClickListener listener;

    public FriendListAdapter(List<Chat> chatList, List<String> usernameList) {
        this.usernameList = usernameList;
        this.chatList = chatList;
    }

    public interface OnItemClickListener {
        void onItemClick(List<Message> messages);
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
        holder.bind(chat, username);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(chat.getMessages());
                }
            }
        });
    }

    @Override
    public int getItemCount() { return chatList.size(); }

    public void removeItem (int position) {
        chatList.remove(position);
        usernameList.remove(position);
        notifyItemRemoved(position);
    }

    public static String formatDate(Date date)
    {
        return DateFormat.getDateTimeInstance().format(date);
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
