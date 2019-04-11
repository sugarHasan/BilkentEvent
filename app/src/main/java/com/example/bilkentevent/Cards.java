package com.example.bilkentevent;

import java.io.Serializable;

public class Cards implements Serializable {
    private String name;
    private String userID;
    private String eventID;
    public Cards(String name ,String userID, String eventID){
        this.name = name;
        this.userID = userID;
        this.eventID = eventID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}

