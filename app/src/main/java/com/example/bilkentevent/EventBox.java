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
    private ArrayList <Event> box;
    private int size;
    //methods
    public void removeTopEvent(){
        box.remove(0);
        size--;
    }
    public Event returnsEvent(){
        Event temp = box.get(0);
        return temp;
    }
    public EventBox() {
        box = new ArrayList<Event>();
        size=0;
    }

    public boolean addEvent(Event addingEvent){
        if(box.add(addingEvent)) {
            size++;
            return true;
        }
        return false;
    }

    public int getSize() {
        return size;
    }

    public boolean removeEvent(Event removingEvent){
        if(box.remove(removingEvent))
            return true;
        return false;
    }

    public boolean containsEvent(Event searchEvent){
        if(box.contains(searchEvent))
            return true;
        return false;
    }
}
