package com.padme.emaone;

import com.google.firebase.Timestamp;

public class Joined_Event_Modal {

    String joined;

    Timestamp datetime;
public Joined_Event_Modal(){}
    public Joined_Event_Modal(String joined, Timestamp datetime) {
        this.joined = joined;
        this.datetime = datetime;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }
}
