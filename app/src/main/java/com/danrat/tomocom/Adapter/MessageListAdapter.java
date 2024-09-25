package com.danrat.tomocom.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danrat.tomocom.Model.Message;
import com.danrat.tomocom.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private final List<Message> messages;
    private final String username;
    private final String senderId;
    private final String receiverId;

    public MessageListAdapter(List<Message> messages, String username, String senderId, String receiverId) {
        this.messages = messages;
        this.username = username;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_chat_right, parent, false);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_chat_left, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.clear();
        Message message = messages.get(position);
        holder.bind(message, username, senderId, receiverId);
    }

    @Override
    public int getItemCount() { return messages.size(); }

    @Override
    public int getItemViewType (int position)
    {
        Message message = messages.get(position);
        if (Objects.equals(message.getSender(), senderId) && Objects.equals(message.getReceiver(), receiverId))
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }

    public static String formatDate(Date date)
    {
        return DateFormat.getDateTimeInstance().format(date);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView textViewUsername;
        private final TextView textViewMessage;
        private final TextView textViewDate;

        ViewHolder(View itemView)
        {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.usernameTV);
            textViewMessage = itemView.findViewById(R.id.messageTV);
            textViewDate = itemView.findViewById(R.id.dateTV);
        }

        void bind (Message message, String username, String senderId, String receiverId)
        {
            textViewUsername.setText(username);
            textViewMessage.setText(message.getMessage());
            textViewDate.setText(formatDate(message.getCreatedAt()));
        }

        public void clear() {
            textViewUsername.setText(R.string.placeholder_username);
            textViewMessage.setText(R.string.placeholder_message);
            textViewDate.setText(R.string.placeholder_date);
        }
    }

    /*private static class MessageReceivedHolder extends RecyclerView.ViewHolder
    {
        private final TextView textViewUsername, textViewMessage, textViewDate;

        MessageReceivedHolder(View itemView)
        {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.usernameTV);
            textViewMessage = itemView.findViewById(R.id.messageTV);
            textViewDate = itemView.findViewById(R.id.dateTV);
        }

        void bind (Message message)
        {
            textViewUsername.setText(message.getSender().getUsername());
            textViewMessage.setText(message.getMessage());
            textViewDate.setText(String.valueOf(message.getCreatedAt()));
        }
    }

    private static class MessageSentHolder extends RecyclerView.ViewHolder
    {
        TextView textViewMessage, textViewDate;

        public MessageSentHolder(View itemView)
        {
            super(itemView);
            textViewMessage=(TextView) itemView.findViewById(R.id.messageTV);
            textViewDate=(TextView) itemView.findViewById(R.id.dateTV);
        }

        public void bind (Message message)
        {
            textViewMessage.setText(message.getMessage());
            textViewDate.setText(String.valueOf(message.getCreatedAt()));
        }
    }*/

}
