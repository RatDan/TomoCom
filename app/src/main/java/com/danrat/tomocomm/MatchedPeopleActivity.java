package com.danrat.tomocomm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danrat.tomocomm.Model.User;
import com.danrat.tomocomm.Adapter.ListAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class MatchedPeopleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched_people);

        // Inicjalizacja danych użytkowników
        userList = new ArrayList<>();
        Set<User.Interest> herodanioSet = EnumSet.of(User.Interest.GAMES, User.Interest.ANIME, User.Interest.MANGA, User.Interest.ONLINEGAMES);
        Set<User.Interest> insanSet = EnumSet.of(User.Interest.DANCING, User.Interest.OUTDOORS, User.Interest.GYM, User.Interest.PROGRAMMING);
        userList.add(new User("Herodanio",23, herodanioSet));
        userList.add(new User("Insan",22, insanSet));
        userList.add(new User("Insan",22, insanSet));
        userList.add(new User("Insan",22, insanSet));
        userList.add(new User("Insan",22, insanSet));
        userList.add(new User("Insan",22, insanSet));
        userList.add(new User("Insan",22, insanSet));
        userList.add(new User("Insan",22, insanSet));
        userList.add(new User("Insan",22, insanSet));

        // Inicjalizacja RecyclerView
        recyclerView = findViewById(R.id.listaRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter(userList);
        recyclerView.setAdapter(adapter);

        // Przycisk wróć
        ImageButton returnButton = (ImageButton)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}