/*
This java class is for creating a person
Created by Hasan Yildirim
20.February.2019
 */
package com.example.bilkentevent;

import android.widget.ImageView;

import java.io.Serializable;

public class Person implements Serializable {
    //constants

    //variables
    private String personID;
    private String name;
    private String motto;

    //methods


    public Person(String personID, String name) {
        this.personID = personID;
        this.name = name;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
