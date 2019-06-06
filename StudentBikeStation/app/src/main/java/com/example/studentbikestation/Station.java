package com.example.studentbikestation;

public class Station {
    String name;
    String location;
    String lock1;
    String lock2;
    String lock3;

    Station(){};
    public Station(String name, String location, String lock1, String lock2, String lock3) {
        this.name = name;
        this.location = location;
        this.lock1 = lock1;
        this.lock2 = lock2;
        this.lock3 = lock3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLock1() {
        return lock1;
    }

    public void setLock1(String lock1) {
        this.lock1 = lock1;
    }

    public String getLock2() {
        return lock2;
    }

    public void setLock2(String lock2) {
        this.lock2 = lock2;
    }

    public String getLock3() {
        return lock3;
    }

    public void setLock3(String lock3) {
        this.lock3 = lock3;
    }

    public int setStationStatus(String status) {
        int id = 0;
        switch (status) {
            case "disconnected":
                id = android.R.drawable.presence_invisible;
                break;
            case "available":
                id = android.R.drawable.presence_online;
                break;
            case "unavailable":
                id = android.R.drawable.presence_offline;
                break;


        }
        return id;

    }
}

