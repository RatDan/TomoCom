package com.danrat.tomocomm.Model;

import java.util.Date;

public class Message {
    String message;
    User sender;
    Date createdAt;

    public Message (String message, User sender)
    {
        this.message=message;
        this.sender=sender;
        this.createdAt= new Date();
    }

    public String getMessage() {return this.message;}
    public User getSender() {return this.sender;}
    public Date getCreatedAt() {return this.createdAt;}
}
