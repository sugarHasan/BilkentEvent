/*
This java class is for creating a Event superclass
Created by Hasan Yildirim
20.February.2019
 */
package com.example.bilkentevent;

public class Event {

    protected Date dayOfEvent;
    protected Time startTime;
    protected Time finishTime;
    protected boolean isOver;
    protected boolean active;
    private String deneme = "Event class";

    public String getDeneme() {
        return deneme;
    }

    public Event(Date dayOfEvent, Time startTime, Time finishTime) {
        this.dayOfEvent = dayOfEvent;
        this.startTime = startTime;
        this.finishTime = finishTime;
        isOver = false;
        active = true;
    }

    public Date getDayOfEvent() {
        return dayOfEvent;
    }

    public void setDayOfEvent(Date dayOfEvent) {
        this.dayOfEvent = dayOfEvent;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Time finishTime) {
        this.finishTime = finishTime;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
