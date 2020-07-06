package com.deneme.gossip.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {


    private String firstName;
    private String lastName;
    private Date joinDate;
    private boolean enable;

    public User() {

    }

    public User( String firstName, String lastName, Date joinDate, boolean enable) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = joinDate;
        this.enable = enable;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
