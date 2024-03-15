package com.danrat.tomocomm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.danrat.tomocomm.Adapter.MessageListAdapter;
import com.danrat.tomocomm.Model.Message;
import com.danrat.tomocomm.Model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageListAdapter adapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageList=new ArrayList<>();
        Set<User.Interest> herodanioSet = EnumSet.of(User.Interest.Gry, User.Interest.Anime, User.Interest.Manga, User.Interest.Gry_Online);
        Set<User.Interest> insanSet = EnumSet.of(User.Interest.Taniec, User.Interest.Spedzanie_czasu_na_zewnatrz, User.Interest.Silownia, User.Interest.Programowanie);
        messageList.add(new Message("xDXD",new User(100,"testowyuż",50,herodanioSet), new Date()));
        messageList.add(new Message("Siemanko",new User(101,"testowyuż2",5321,insanSet),new Date()));

        recyclerView = (RecyclerView) findViewById(R.id.chatRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageListAdapter(messageList);
        recyclerView.setAdapter(adapter);

        ImageButton returnButton = (ImageButton)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}