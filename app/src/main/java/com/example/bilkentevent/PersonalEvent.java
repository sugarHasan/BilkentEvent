package com.example.bilkentevent;

public class PersonalEvent extends Event {

    //variables


    //methods

    public PersonalEvent(Date dayOfEvent, Time startTime, Time finishTime,String topic) {
        super( dayOfEvent, startTime.toString(), finishTime.toString(), topic);
    }


}
