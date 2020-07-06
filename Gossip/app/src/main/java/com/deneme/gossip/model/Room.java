package com.deneme.gossip.model;

import java.io.Serializable;
import java.util.Date;

public class Room implements Serializable {

    private String name;
    private Date time;
    private long userCount;

    public Room() {
    }

    public Room(String name, Date time, long userCount) {
        this.name = name;
        this.time = time;
        this.userCount = userCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", userCount=" + userCount +
                '}';
    }
}

























