package com.danrat.tomocom;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danrat.tomocom.Adapter.FriendListAdapter;
import com.danrat.tomocom.Adapter.MessageListAdapter;
import com.danrat.tomocom.Model.Message;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.activity.OnBackPressedCallback;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ChatFragment extends Fragment {

    private static final String messagesArg = "messages";
    private static final String usernameArg = "username";
    private static final String uidArg = "uid";
    private List<Message> messages;
    private String username;
    private String uid;
    private RecyclerView recyclerView;
    private MessageListAdapter adapter;

    public ChatFragment() {}

    public static ChatFragment newInstance(List<Message> messages, String username, String uid) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();

        args.putSerializable(messagesArg, (Serializable) messages);
        args.putSerializable(usernameArg, username);
        args.putSerializable(uidArg, uid);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            messages = (List<Message>) getArguments().getSerializable(messagesArg);
            username = (String) getArguments().getSerializable(usernameArg);
            uid = (String) getArguments().getSerializable(uid);
        }

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation);
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            public void handleOnBackPressed() {
                if (getParentFragment() != null && getParentFragmentManager().getBackStackEntryCount() > 0) {
                    getParentFragmentManager().popBackStack();
                } else {
                    requireActivity().finish();
                }
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DocumentReference documentReference = fireStore.collection("chat_rooms").document(userID);

        TextView textView = view.findViewById(R.id.usernameTV);
        textView.append(" " + username);

        recyclerView = view.findViewById(R.id.chatRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MessageListAdapter(messages, username, userID, uid);
        recyclerView.setAdapter(adapter);

        return view;
    }
}