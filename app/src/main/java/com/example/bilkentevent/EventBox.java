/*
This java class is for creating a box of events
Created by Hasan Yildirim
20.February.2019
 */
package com.example.bilkentevent;

import java.util.ArrayList;

public class EventBox {
    //constants

    //variables
    ArrayList <Event> box;

    //methods

    public EventBox() {
        box = new ArrayList<Event>();
    }

    public boolean addEvent(Event addingEvent){
        if(box.add(addingEvent))
            return true;
        return false;
    }

    public boolean removeEvent(Event removingEvent){
        if(box.remove(removingEvent))
            return true;
        return false;
    }
}
