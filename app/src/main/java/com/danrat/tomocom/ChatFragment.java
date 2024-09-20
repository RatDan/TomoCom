package com.danrat.tomocom;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danrat.tomocom.Adapter.MessageListAdapter;
import com.danrat.tomocom.Model.Message;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.List;

public class ChatFragment extends Fragment {

    private static final String messagesArg = "messages";
    private List<Message> messages;

    public ChatFragment() {}

    public static ChatFragment newInstance(List<Message> messages) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();

        args.putSerializable(messagesArg, (Serializable) messages);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            messages = (List<Message>) getArguments().getSerializable(messagesArg);
        }

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigation);
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        TextView textView = view.findViewById(R.id.usernameTV);
        textView.append(" coś");

        RecyclerView recyclerView = view.findViewById(R.id.chatRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MessageListAdapter adapter = new MessageListAdapter(messages);
        recyclerView.setAdapter(adapter);

        return view;
    }
}