package com.danrat.tomocom.Model;

import com.google.firebase.firestore.Exclude;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class User {
    private String uid="";
    private String username="";
    private int age=0;
    private String description="";
    private String profileImageUrl;

    private List<String> interests = Collections.emptyList();
    private List<String> subinterests = Collections.emptyList();
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
    public void setDescription (String description) { this.description = description; }
    public void setInterests (List<String> interests) { this.interests = interests; }
    public void setSubInterests (List<String> subinterests) { this.subinterests = subinterests; }
    public void setAge (int age) { this.age = age; }
    public void setUsername (String username) { this.username = username; }
    public void setProfileImageUrl (String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public int getAge () {
        return age;
    }
    public String getDescription() { return  description; }
    public List<String> getInterests () { return interests; }
    public List<String> getSubInterests () { return subinterests; }
    public List<String> getSkipped () { return skipped; }
    public List<String> getFriends () { return friends; }
    public String getProfileImageUrl () { return profileImageUrl; }

    @Exclude
    public String getInterestsString() {
        StringBuilder temp = new StringBuilder();
        Iterator<String> iIterator = interests.iterator();
        Iterator<String> sIterator = subinterests.iterator();

        while (iIterator.hasNext() && sIterator.hasNext())
        {
            String interest = iIterator.next();
            String subInterest = sIterator.next();
            temp.append(" \n").append(interest).append(": ").append(subInterest).append(", ");
        }

        if (temp.length() != 0)
            temp.deleteCharAt(temp.length()-2);

        return temp.toString();
    }

}