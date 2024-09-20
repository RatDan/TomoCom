package com.danrat.tomocom.Model;

import androidx.annotation.NonNull;

public enum Interests
{
    Dancing ("Taniec"),
    Fashion("Moda"),
    Shows ("Seriale"),
    Traveling ("Podróżowanie"),
    Art ("Sztuka"),
    Games ("Gry"),
    Sport ("Sport"),
    Animals ("Zwierzęta"),
    Cooking ("Gotowanie"),
    Movies ("Filmy"),
    Anime ("Anime"),
    Manga ("Manga"),
    Music ("Muzyka"),
    Nature ("Przyroda"),
    Programming ("Programowanie"),
    Online_Games ("Gry Online"),
    Gym ("Siłownia"),
    Writing ("Pisarstwo"),
    Fitness ("Fitness"),
    Outside_Activities ("Spędzanie czasu na zewnątrz"),
    Concerts ("Koncerty"),
    Science ("Nauki ścisłe"),
    Books ("Książki"),
    Camping ("Kemping"),
    Board_Games ("Gry planszowe"),
    Fishing ("Wędkarstwo"),
    Hiking ("Turystyka piesza");

    private final String interestName;

    Interests (String i)
    {
        interestName = i;
    }

    @NonNull
    public String toString() {
        return this.interestName;
    }
}
