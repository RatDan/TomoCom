package com.danrat.tomocom.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danrat.tomocom.Model.Chat;
import com.danrat.tomocom.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private OnMenuItemClickListener menuItemClickListener;
    private List<Chat> chatList;
    private List<String> usernameList;
    private List<String> uidList;
    private List<String> descriptionList;
    private List<String> profilePictureList;
    private static String currentUserId;
    private OnItemClickListener listener;

    public FriendListAdapter(List<Chat> chatList, List<String> usernameList, List<String> uidList, List<String> descriptionList, List<String> profilePictureList, String currentUserId, OnMenuItemClickListener menuItemClickListener) {
        this.chatList = chatList;
        this.usernameList = usernameList;
        this.uidList = uidList;
        this.profilePictureList = profilePictureList;
        this.menuItemClickListener = menuItemClickListener;
        this.descriptionList = descriptionList;
        FriendListAdapter.currentUserId = currentUserId;
    }

    public interface OnItemClickListener {
        void onItemClick(String username, String uid, String cid, String description, String profilePictureUrl);
    }

    public interface OnMenuItemClickListener {
        void onRemoveFriend(String uid, String cid);
        void onRemoveAndBlockFriend(String uid, String cid);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.clear();
        Chat chat = chatList.get(position);
        String username = usernameList.get(position);
        String description = descriptionList.get(position);
        String uid = uidList.get(position);
        String profilePictureUrl = profilePictureList.get(position);
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Picasso.get().load(profilePictureUrl).placeholder(R.drawable.nav_profile).noFade().into(holder.profileImageView);
        } else {
            holder.profileImageView.setImageResource(R.drawable.nav_profile);
        }
        holder.bind(chat, username);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(username, uid, chat.getCid(), description, profilePictureUrl);
            }
        });

        holder.moreButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.more_options_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.remove_friend:
                            menuItemClickListener.onRemoveFriend(uid, chat.getCid());
                            return true;
                        case R.id.remove_and_block_friend:
                            menuItemClickListener.onRemoveAndBlockFriend(uid, chat.getCid());
                            return true;
                        default:
                            return false;
                    }
                }
            });

            popupMenu.show();
        });
    }



    @Override
    public int getItemCount() { return chatList.size(); }

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
    public void updateData(List<Chat> newChatRooms, List<String> newUsernames, List<String> newUids, List<String> newDescriptions, List<String> newProfilePictureList, String newUid, OnMenuItemClickListener newMenuItemClickListener) {
        this.chatList = newChatRooms;
        this.usernameList = newUsernames;
        this.uidList = newUids;
        this.profilePictureList = newProfilePictureList;
        this.descriptionList = newDescriptions;
        currentUserId = newUid;
        this.menuItemClickListener = newMenuItemClickListener;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView textViewName ;
        private final TextView textViewMessage;
        private final TextView textViewDate;
        private final ImageView profileImageView;
        private final ImageButton moreButton;

        public ViewHolder(final View itemView) {
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

        public void bind(Chat chat, String username) {
            String senderUid = chat.getLastMessage().getSender();
            String lastMessage = chat.getLastMessage().getMessage();

            if (senderUid.equals(currentUserId)) {
                lastMessage = "Ty: " + lastMessage;
            }

            textViewName.setText(username);
            textViewMessage.setText(lastMessage);
            textViewDate.setText(formatDate(chat.getLastMessage().getCreatedAt()));
        }

    }
}
