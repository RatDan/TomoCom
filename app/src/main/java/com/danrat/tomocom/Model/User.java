package com.danrat.tomocom.Model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class User {
    private String uid="";
    private final String email;
    private final String username;
    private final int age;
    private String description="";

    private final List<Interests> interests = Collections.emptyList();
    private List<String> friends = Collections.emptyList();
    private List<String> skipped = Collections.emptyList();

    public User (String email, String username, int age)
    {
        this.email = email;
        this.username = username;
        this.age = age;
    }

    public User ()
    {
        this.email="";
        this.username="";
        this.age=0;
    }

    public void setUid (String uid) {
        this.uid=uid;
    }
    public void setSkipped (List<String> skipped) { this.skipped = skipped; }
    public void setFriends (List<String> friends) { this.friends = friends; }
    public void setDescription (String description) { this.description=description; }

    public String getUid() { return uid; }
    public String getEmail() { return email; }
    public String getUsername() {
        return username;
    }
    public int getAge () {
        return age;
    }
    public String getDescription() { return  description; }
    public List<Interests> getInterests () { return interests; }
    public List<String> getSkipped () { return skipped; }
    public List<String> getFriends () { return friends; }

    @Exclude
    public String getInterestsString() {
        StringBuilder temp = new StringBuilder();
        for (Interests interest : interests) {
            temp.append(interest.toString()).append(", ");
        }
        return temp.toString();
    }

}