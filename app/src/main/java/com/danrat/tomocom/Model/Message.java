package com.danrat.tomocom.Model;

import java.util.Date;

public class Message {
    String senderId;
    String receiverId;
    String message;
    Date createdAt;

    public Message (String senderId, String receiverId, String message) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.createdAt = new Date();
    }

    public Message () {
        this.senderId="";
        this.receiverId="";
        this.message="";
        this.createdAt=new Date();
    }

    public void setSender(String senderId) { this.senderId=senderId; }
    public void setReceiver(String receiverId) { this.receiverId=receiverId; }

    public String getSender() { return this.senderId; }
    public String getReceiver() { return this.receiverId; }
    public String getMessage() { return this.message; }
    public Date getCreatedAt() { return this.createdAt; }
}
