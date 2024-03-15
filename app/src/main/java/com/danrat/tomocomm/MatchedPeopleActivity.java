package com.danrat.tomocomm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danrat.tomocomm.Model.User;
import com.danrat.tomocomm.Adapter.UserListAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class MatchedPeopleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserListAdapter adapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched_people);

        // Inicjalizacja danych użytkowników
        userList = new ArrayList<>();
        Set<User.Interest> herodanioSet = EnumSet.of(User.Interest.Gry, User.Interest.Anime, User.Interest.Manga, User.Interest.Gry_Online);
        Set<User.Interest> insanSet = EnumSet.of(User.Interest.Taniec, User.Interest.Spedzanie_czasu_na_zewnatrz, User.Interest.Silownia, User.Interest.Programowanie);
        userList.add(new User(1,"Herodanio",23, herodanioSet));
        userList.add(new User(2,"Insan",22, insanSet));
        userList.add(new User(3,"Insan",22, insanSet));
        userList.add(new User(4,"Insan",22, insanSet));
        userList.add(new User(5,"Insan",22, insanSet));
        userList.add(new User(6,"Insan",22, insanSet));
        userList.add(new User(7,"Insan",22, insanSet));
        userList.add(new User(8,"Insan",22, insanSet));
        userList.add(new User(9,"Insan",22, insanSet));

        // Inicjalizacja RecyclerView
        recyclerView = findViewById(R.id.listaRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserListAdapter(userList);
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