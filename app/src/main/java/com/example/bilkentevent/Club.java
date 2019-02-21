/*
This java class is for creating a club
Created by Hasan Yildirim
20.February.2019
 */
package com.example.bilkentevent;

import android.widget.ImageView;

public class Club {
    //constants

    //variables
    private String name;
    private String motto;
    private String information;
    private ImageView clubPhoto;
    //methods

    public Club(String name,String motto, String information, ImageView clubPhoto){
        this.name = name;
        this.motto = motto;
        this.information = information;
        this.clubPhoto = clubPhoto;
    }

    public String getName(){
        return name;
    }

    public String getMotto(){
        return motto;
    }

    public String getInformation(){
        return information;
    }

    public ImageView getClubPhoto(){
        return clubPhoto;
    }



}
