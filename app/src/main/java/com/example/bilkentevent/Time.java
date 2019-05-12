/*
This java class is for creating a time as hour-minute
Created by Hasan Yildirim
20.February.2019
 */
package com.example.bilkentevent;

public class Time {
    //constants

    //variables
    int hour;
    int minute;
    //methods

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
