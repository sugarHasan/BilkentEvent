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
    private Club club;
    private int gePoint;
    private int popularity;
    private ArrayList<Person> participants;

    //methods

    public ClubEvent(Date dayOfEvent, Time startTime, Time finishTime, Club club, int gePoint) {
        super( dayOfEvent, startTime, finishTime);
        this.club = club;
        this.gePoint = gePoint;
        popularity = 0;
        participants = new ArrayList<Person>();
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public int getGePoint() {
        return gePoint;
    }

    public void setGePoint(int gePoint) {
        this.gePoint = gePoint;
    }

    public boolean addParticipant(Person participant){
        if(participants.add(participant)) {
            popularity++;
            return true;
        }
        return false;
    }

    public boolean removeParticipant(Person participant){
        if (participants.remove(participant)) {
            popularity--;
            return true;
        }
        return false;
    }


}
