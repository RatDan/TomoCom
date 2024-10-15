package com.danrat.tomocom.Model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class User {
    private String uid="";
    private String username="";
    private int age=0;
    private String description="";
    private String profileImageUrl;

    private final List<Interests> interests = Collections.emptyList();
    private List<String> friends = Collections.emptyList();
    private List<String> skipped = Collections.emptyList();

    public User (String username, int age) {
        this.username = username;
        this.age = age;
    }

    public User () {
        this.username="";
        this.age=0;
    }

    public User (String uid, List<String> friends) {
        this.uid = uid;
        this.friends = friends;
    }

    public void setUid (String uid) {
        this.uid=uid;
    }
    public void setSkipped (List<String> skipped) { this.skipped = skipped; }
    public void setFriends (List<String> friends) { this.friends = friends; }
    public void setDescription (String description) { this.description=description; }
    public void setProfileImageUrl (String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public int getAge () {
        return age;
    }
    public String getDescription() { return  description; }
    public List<Interests> getInterests () { return interests; }
    public List<String> getSkipped () { return skipped; }
    public List<String> getFriends () { return friends; }
    public String getProfileImageUrl () { return profileImageUrl; }

    @Exclude
    public String getInterestsString() {
        StringBuilder temp = new StringBuilder();
        for (Interests interest : interests) {
            temp.append(interest.toString()).append(", ");
        }
        temp.deleteCharAt(temp.length()-2);
        return temp.toString();
    }

}