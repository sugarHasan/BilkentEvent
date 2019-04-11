/*
This java class is for creating a club event that has super class of event
Created by Hasan Yildirim
20.February.2019
 */
package com.example.bilkentevent;

import java.util.ArrayList;

public class ClubEvent extends Event {
    //constants

    //variables
    private String clubID;
    private String eventID;
    private long popularity;

    //methods

    public ClubEvent(Date dayOfEvent, Time startTime, Time finishTime, String clubID, String eventId,long popularity) {
        super( dayOfEvent, startTime, finishTime);
        this.clubID = clubID;
        this.eventID = eventId;
        this.popularity = popularity ;
    }

    public String getClubID() {
        return clubID;
    }

    public void setClubID(String clubID) {
        this.clubID = clubID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }
}
