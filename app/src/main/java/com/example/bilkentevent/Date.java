/*
This java class is for creating a date as day-month-year
Created by Hasan Yildirim
20.February.2019
 */
package com.example.bilkentevent;

public class Date {
    //constants


    //variables
    private int day;
    private int month;
    private int year;



    //methods

    public Date(int day, int month, int year ) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }



}
