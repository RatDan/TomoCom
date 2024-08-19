package com.danrat.tomocomm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danrat.tomocomm.Model.Message;
import com.danrat.tomocomm.Model.User;
import com.danrat.tomocomm.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {
    //private Context context;
    private final List<Message> messages;

    //public MessageListAdapter (Context context, List<Message> messages) {
        //this.context=context;
        //this.messages=messages;
    //}

    public MessageListAdapter(List<Message> messages) { this.messages = messages; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view; // = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, parent, false);
        if (viewType == 1)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_chat_right, parent, false);
            return new ViewHolder(view);
        }
        else if (viewType == 2)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_chat_left, parent, false);
            return new ViewHolder(view);
        }
        return null;
        //view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_chat_right, parent, false);
        //return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messages.get(position);
        /*switch (holder.getItemViewType()) {
           case 1:
                holder.bind(message);
                break;
            case 2:
                holder.bind(message);
                break;

        }*/
        holder.bind(message);
    }

    @Override
    public int getItemCount() {return messages.size();}

    @Override
    public int getItemViewType (int position)
    {
        Message message = (Message)messages.get(position);
        if (message.getSender().getId()%2==0)
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
        private final TextView textViewUsername, textViewMessage, textViewDate;

        ViewHolder(View itemView)
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
            textViewDate.setText(formatDate(message.getCreatedAt()));
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
