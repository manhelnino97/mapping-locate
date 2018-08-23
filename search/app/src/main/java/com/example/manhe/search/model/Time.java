package com.example.manhe.search.model;

public class Time {
    private String time;
    boolean checkDuplicate;

    public Time(String time, boolean checkDuplicate) {
        this.time = time;
        this.checkDuplicate = checkDuplicate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
