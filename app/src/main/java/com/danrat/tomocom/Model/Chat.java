package com.danrat.tomocom.Model;

import com.google.firebase.firestore.DocumentId;

import java.util.Collections;
import java.util.List;

public class Chat {
    private String cid = "";
    private final List<String> members;
    private final List<Message> messages;
    private final Message lastMessage;

    public Chat (List<String> members, List<Message> messages) {
        this.members = members;
        this.messages = messages;
        this.lastMessage = messages.get(messages.size()-1);
    }

    public Chat (List<String> members) {
        this.members = members;
        this.messages = Collections.emptyList();
        this.lastMessage = new Message();
    }

    public Chat () {
        this.members = Collections.emptyList();
        this.messages = Collections.emptyList();
        this.lastMessage = new Message();
    }

    @DocumentId
    public void setCid(String cid) { this.cid = cid; }

    @DocumentId
    public String getCid() { return this.cid; }
    public List<String> getMembers() { return this.members; }
    public List<Message> getMessages() { return this.messages; }
    public Message getLastMessage() { return this.lastMessage; }

}
