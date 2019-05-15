/*
This java class is for creating a club event that has super class of event
Created by Hasan Yildirim
20.February.2019
 */
package com.example.bilkentevent;


import java.io.Serializable;

public class ClubEvent extends Event{
    //constants

    //variables
    private String location;
    private String clubID;
    private String eventID;
    private int popularity;

    //methods

    public ClubEvent(Date dayOfEvent, String startTime, String finishTime,String topic, String clubID, String eventId,String location, int popularity) {
        super( dayOfEvent, startTime, finishTime,topic);
        this.clubID = clubID;
        this.eventID = eventId;
        this.popularity = popularity ;
        this.location = location;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
}
