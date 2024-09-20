package com.danrat.tomocom.Model;

import java.util.Collections;
import java.util.List;

public class Chat {
    //private final int cid = 0;
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

    //public int getCid() { return this.cid; }
    public List<String> getMembers() { return this.members; }
    public List<Message> getMessages() { return this.messages; }
    public Message getLastMessage() { return this.lastMessage; }

}
