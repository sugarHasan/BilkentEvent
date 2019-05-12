/*
This java class is for creating a Event superclass
Created by Hasan Yildirim
20.February.2019
 */
package com.example.bilkentevent;

import java.io.Serializable;

public class Event{

    protected Date dayOfEvent;
    protected Time startTime;
    protected Time finishTime;
    protected boolean isOver;
    protected boolean active;
    private String topic ;



    public Event(Date dayOfEvent, Time startTime, Time finishTime, String topic) {
        this.dayOfEvent = dayOfEvent;
        this.startTime = startTime;
        this.finishTime = finishTime;
        isOver = false;
        active = true;
        this.topic = topic;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


}
