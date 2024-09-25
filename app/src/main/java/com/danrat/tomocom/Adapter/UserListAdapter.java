package com.danrat.tomocom.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danrat.tomocom.Model.User;
import com.danrat.tomocom.R;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private List<User> userList;
    private List<Integer> matchLevelList;
    private OnItemClickListener listener;

    public UserListAdapter(List<User> userList, List<Integer> matchLevelList) {
        this.userList = userList;
        this.matchLevelList = matchLevelList;
    } //Konstruktor

    public interface OnItemClickListener {
        void onItemClick(String userName, int age, String interests, String description);
        void onAddClick(String uid, String username, int position);
        void onSkipClick(String uid, String username, int position);
    } //Interfejs onItemClick

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.clear();
        User user = userList.get(position);
        int matchLevel = matchLevelList.get(position);
        holder.bind(user, matchLevel);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(user.getUsername(), user.getAge(), user.getInterestsString(), user.getDescription());
                }
            }
        });

        holder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAddClick(user.getUid(), user.getUsername(), holder.getAdapterPosition());
                }
            }
        });

        holder.buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSkipClick(user.getUid(), user.getUsername(), holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void removeItem (int position) {
        userList.remove(position);
        matchLevelList.remove(position);
        notifyItemRemoved(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<User> newUsers, List<Integer> newMatchLevels) {
        this.userList = newUsers;
        this.matchLevelList = newMatchLevels;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewAge;
        private final TextView textViewInterests;
        private final TextView textViewMatchLevel;
        private final ProgressBar progressBar;
        private final ImageButton buttonAdd;
        private final ImageButton buttonSkip;

        public ViewHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.TV1);
            textViewAge = itemView.findViewById(R.id.TV2);
            textViewInterests = itemView.findViewById(R.id.TV3);
            textViewMatchLevel = itemView.findViewById(R.id.TV4);
            progressBar = itemView.findViewById(R.id.progressBar);
            buttonAdd = itemView.findViewById(R.id.addButton);
            buttonSkip = itemView.findViewById(R.id.skipButton);
        }

        public void clear() {
            textViewName.setText("");
            textViewAge.setText(R.string.card_age);
            textViewInterests.setText(R.string.card_interests);
            textViewMatchLevel.setText(R.string.match_level);
            progressBar.setProgress(0);
        }

        public void bind(User user, int matchLevel) {
            textViewName.setText(user.getUsername());
            textViewAge.append(" " + String.valueOf(user.getAge()));
            textViewInterests.append(" " + user.getInterestsString());
            textViewMatchLevel.append(" " + String.valueOf(matchLevel) + "%");
            progressBar.setProgress(matchLevel);
        }
    }
}
