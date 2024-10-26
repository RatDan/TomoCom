package com.danrat.tomocom.Model;

import java.util.Collections;
import java.util.List;

public class Chat {
    private final String cid;
    private final List<String> members;
    private final List<Message> messages;
    private final Message lastMessage;

    public Chat (String cid, List<String> members) {
        this.cid = cid;
        this.members = members;
        this.messages = Collections.emptyList();
        this.lastMessage = new Message();
    }

    public Chat () {
        this.cid = "";
        this.members = Collections.emptyList();
        this.messages = Collections.emptyList();
        this.lastMessage = new Message();
    }

    public String getCid() { return this.cid; }
    public List<String> getMembers() { return this.members; }
    public List<Message> getMessages() { return this.messages; }
    public Message getLastMessage() { return this.lastMessage; }

}
