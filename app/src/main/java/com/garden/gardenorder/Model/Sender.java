package com.garden.gardenorder.Model;



/**
 * Created by Faggot on 11/25/2017.
 */

public class Sender {
    public String to;
    public Notification notification;

    public Sender(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }
}
