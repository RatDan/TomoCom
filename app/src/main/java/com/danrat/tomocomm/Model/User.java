package com.danrat.tomocomm.Model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class User {
    public enum Interest
    {
        DANCING,
        FASHION,
        SHOWS,
        TRAVELLING,
        ART,
        GAMES,
        SPORT,
        ANIMALS,
        COOKING,
        MOVIES,
        ANIME,
        MANGA,
        MUSIC,
        NATURE,
        PROGRAMMING,
        ONLINEGAMES,
        GYM,
        WRITING,
        FITNESS,
        OUTDOORS,
        CONCERTS,
        SCIENCE,
        BOOKS,
        CAMPING,
        BOARDGAMES,
        FISHING,
        HIKING
    }

    private String username;
    private int age;
    private Set<Interest> interests; //Podobno set używa się do Enumów

    public User (String username, int age, Set<Interest> interests)
    {
        this.username=username;
        this.age=age;
        this.interests=interests;
    }

    public String getUsername() {
        return username;
    }

    public int getAge () {
        return age;
    }

    public String getInterests() {
        String temp="";
        Iterator<Interest> interestIterator = interests.iterator();
        while (interestIterator.hasNext()) {
            temp=temp + " " + interestIterator.next();
        }
        return temp;
    }
}
