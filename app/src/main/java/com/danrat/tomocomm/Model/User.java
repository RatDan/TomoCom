package com.danrat.tomocomm.Model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class User {
    private final int id;

    public enum Interest
    {
        Taniec,
        Moda,
        Seriale,
        Podrozowanie,
        Sztuka,
        Gry,
        Sport,
        Zwierzeta,
        Gotowanie,
        Filmy,
        Anime,
        Manga,
        Music,
        Przyroda,
        Programowanie,
        Gry_Online,
        Silownia,
        Pisarstwo,
        Fitness,
        Spedzanie_czasu_na_zewnatrz,
        Koncerty,
        Nauki_scisle,
        Ksiażki,
        Kemping,
        Gry_planszowe,
        Wedkarstwo,
        Turystyka_piesza
    }

    private final String username;
    private final int age;
    private final Set<Interest> interests; //Podobno set używa się do Enumów

    public User (int id, String username, int age, Set<Interest> interests)
    {
        this.id=id;
        this.username=username;
        this.age=age;
        this.interests=interests;
    }

    public int getId() {return id;}
    public String getUsername() {
        return username;
    }
    public int getAge () {
        return age;
    }
    public String getInterests() {
        StringBuilder temp= new StringBuilder();
        Iterator<Interest> interestIterator = interests.iterator();
        while (interestIterator.hasNext()) {
            temp.append(" ").append(interestIterator.next());
        }
        return temp.toString();
    }
}
