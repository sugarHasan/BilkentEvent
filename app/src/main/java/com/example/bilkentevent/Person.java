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

    //methods
    Person(String mail, String password){
        this.name = name;
        this.password = password;
        gePoint = 0;

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








}
