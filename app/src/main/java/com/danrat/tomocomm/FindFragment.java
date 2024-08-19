package com.danrat.tomocomm;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danrat.tomocomm.Adapter.UserListAdapter;
import com.danrat.tomocomm.Model.User;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class FindFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);

        // Inicjalizacja danych użytkowników
        List<User> userList = new ArrayList<>();
        Set<User.Interest> setOne = EnumSet.of(User.Interest.Gry, User.Interest.Anime, User.Interest.Manga, User.Interest.Gry_Online);
        Set<User.Interest> setTwo = EnumSet.of(User.Interest.Taniec, User.Interest.Spedzanie_czasu_na_zewnatrz, User.Interest.Silownia, User.Interest.Programowanie);
        userList.add(new User(1,"User1",23, setOne));
        userList.add(new User(3,"User2",24, setTwo));
        userList.add(new User(2,"User3",25, setOne));
        userList.add(new User(4,"User4",26, setTwo));
        userList.add(new User(5,"User5",27, setOne));
        userList.add(new User(6,"User6",28, setTwo));
        userList.add(new User(7,"User7",29, setOne));
        userList.add(new User(8,"User8",30, setTwo));
        userList.add(new User(9,"User9",31, setOne));

        // Inicjalizacja RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.listaRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        UserListAdapter adapter = new UserListAdapter(userList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}