package com.danrat.tomocomm.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danrat.tomocomm.Model.User;
import com.danrat.tomocomm.ProfileDialogFragment;
import com.danrat.tomocomm.R;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private final List<User> userList;

    public UserListAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.clear();
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewAge;
        private final TextView textViewInterests;

        public ViewHolder(final View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.TV1);
            textViewAge = itemView.findViewById(R.id.TV2);
            textViewInterests = itemView.findViewById(R.id.TV3);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), ProfileDialogFragment.class));
                }
            });
        }

        public void clear() {
            textViewName.setText("");
            textViewAge.setText(R.string.card_age);
            textViewInterests.setText(R.string.card_interests);
        }

        public void bind(User user) {
            textViewName.setText(user.getUsername());
            textViewAge.append(String.valueOf(user.getAge()));
            textViewInterests.append(user.getInterests());
        }

    }
}
