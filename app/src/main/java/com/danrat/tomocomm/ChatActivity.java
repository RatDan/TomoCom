package com.danrat.tomocomm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Set<User.Interest> herodanioSet = EnumSet.of(User.Interest.Gry, User.Interest.Anime, User.Interest.Manga, User.Interest.Gry_Online);
        Set<User.Interest> insanSet = EnumSet.of(User.Interest.Taniec, User.Interest.Spedzanie_czasu_na_zewnatrz, User.Interest.Silownia, User.Interest.Programowanie);

        User user1 = new User(100,"User1",24,herodanioSet);
        User user2 = new User(101,"User2",26,insanSet);

        TextView textView = findViewById(R.id.usernameTV);
        textView.append(" " + user2.getUsername());
        List<Message> messageList = new ArrayList<>();

        messageList.add(new Message("Witaj! Co tam u ciebie?",user1));
        messageList.add(new Message("Witaj kolego. Jakoś leci",user2));
        messageList.add(new Message("Byłbyś zainteresowany wyjściem na piłkę?",user2));
        messageList.add(new Message("Pewnie!",user1));
        messageList.add(new Message("O której?",user1));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chatRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MessageListAdapter adapter = new MessageListAdapter(messageList);
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