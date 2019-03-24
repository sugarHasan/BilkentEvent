/*
This java class is for creating a Event of that will happen weekly
Created by Hasan Yildirim
20.February.2019
 */
package com.example.bilkentevent;

public class WeeklyEvent extends Event{
    //constants
    private int[] days = {0,1,2,3,4,5,6}; //0 as monday, ... , 6 as sunday
    //variables
    int dayOfWeek;
    //methods

    public WeeklyEvent(Date dayOfEvent, Time startTime, Time finishTime,int dayOfWeek) {
        super(dayOfEvent, startTime, finishTime);
        this.dayOfWeek = dayOfWeek;
    }

    public boolean isThatDay(){
        return true;
    }
}
