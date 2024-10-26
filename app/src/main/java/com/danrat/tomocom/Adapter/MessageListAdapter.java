package com.danrat.tomocom.Adapter;

import android.annotation.SuppressLint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danrat.tomocom.Model.Message;
import com.danrat.tomocom.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private List<Message> messages;
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

        boolean sameUserAsPrevious = false;
        boolean withinFiveMinutes = false;
        boolean shouldShowDate = true;

        if (position > 0) {
            Message previousMessage = messages.get(position - 1);

            sameUserAsPrevious = message.getSender().equals(previousMessage.getSender());

            long timeDifference = message.getCreatedAt().getTime() - previousMessage.getCreatedAt().getTime();
            withinFiveMinutes = timeDifference <= 5 * 60 * 1000;

            shouldShowDate = !withinFiveMinutes;
        }

        boolean shouldShowUsername = !sameUserAsPrevious || shouldShowDate;

        holder.bind(message, shouldShowUsername ? username : null, shouldShowDate);
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

    public static String formatDate(Date date) {
        Calendar messageCal = Calendar.getInstance();
        messageCal.setTime(date);

        Calendar today = Calendar.getInstance();

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm", new Locale("pl", "PL"));
        SimpleDateFormat dateFormatSameYear = new SimpleDateFormat("dd MMMM, HH:mm", new Locale("pl", "PL"));


        if (isSameDay(messageCal, today)) {
            return "Dzisiaj, " + timeFormat.format(date);
        }

        Calendar yesterday = (Calendar) today.clone();
        yesterday.add(Calendar.DATE, -1);
        if (isSameDay(messageCal, yesterday)) {
            return "Wczoraj, " + timeFormat.format(date);
        }

        if (isSameYear(messageCal, today))
            return dateFormatSameYear.format(date);
        else
            return dateFormat.format(date);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isSameYear(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Message> newMessages) {
        this.messages = new ArrayList<>(newMessages);
        notifyDataSetChanged();
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

        void bind(Message message, String username, boolean shouldShowDate) {
            if (textViewUsername != null) {
                if (username != null) {
                    textViewUsername.setText(username);
                    textViewUsername.setVisibility(View.VISIBLE);
                } else {
                    textViewUsername.setVisibility(View.GONE);
                }
            }

            textViewMessage.setText(message.getMessage());

            if (textViewDate != null) {
                if (shouldShowDate) {
                    textViewDate.setText(formatDate(message.getCreatedAt()));
                    textViewDate.setVisibility(View.VISIBLE);
                } else {
                    textViewDate.setVisibility(View.GONE);
                }
            }
        }


        public void clear() {
            if (textViewUsername != null) {
                textViewUsername.setText(R.string.placeholder_username);
                textViewUsername.setVisibility(View.VISIBLE);
            }
            textViewMessage.setText(R.string.placeholder_message);
            textViewDate.setText(R.string.placeholder_date);
            textViewDate.setVisibility(View.VISIBLE);
        }
    }

}
