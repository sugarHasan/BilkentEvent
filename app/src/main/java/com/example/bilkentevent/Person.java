/*
This java class is for creating a person
Created by Hasan Yildirim
20.February.2019
 */
package com.example.bilkentevent;

import android.widget.ImageView;

public class Person {
    //constants

    //variables
    private String name;
    private String motto;
    private String mail;
    private  String password;
    private int gePoint;
    private ImageView profilePicture;
    private int characteristicId;
    private EventBox liked;
    private EventBox disliked;
    //methods
    Person(String mail, String password){
        this.name = name;
        this.password = password;
        gePoint = 0;
        liked = new EventBox();
        disliked = new EventBox();
    }

    public void setName(String name){
        this.name = name;
    }

    public void setMotto(String motto){
        this.motto = motto;
    }

    public void setProfilePicture(ImageView profilePicture){
        this.profilePicture = profilePicture;
    }

    public int getCharacteristicId(){
        return characteristicId;
    }

    public String getName(){
        return name;
    }

    public String getMotto(){
        return motto;
    }

    public boolean likeEvent(Event addingEvent){
        if(liked.addEvent(addingEvent))
            return true;
        return false;
    }

    public boolean dislikeEvent(Event dislikedEvent){
        if(disliked.addEvent(dislikedEvent))
            return true;
        return false;
    }

    public boolean removeEvent(Event removingEvent){
        if(liked.removeEvent(removingEvent))
            return true;
        return false;
    }

    public void increasPoint(int addPoint){
        gePoint += addPoint;
    }

    public EventBox thatDayEvents(Date date){
        EventBox result = new EventBox();
        for(int i =0 ; i < liked.getSize();i++ ){

        }

        return result;
    }

}
