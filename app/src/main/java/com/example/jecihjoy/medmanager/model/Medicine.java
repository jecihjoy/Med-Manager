package com.example.jecihjoy.medmanager.model;

/**
 * Created by Jecihjoy on 4/2/2018.
 */

public class Medicine {
    public String name, desc,startDate, endDate, starttime,frequency, month;
    public long medId;
    public int days, duration;

    public Medicine(long medId, String name, String desc, String frequency,
                    String startDate, int days, String endDate, String starttime,
                    int duration, String month){
        this.name = name;
        this.desc = desc;
        this.medId = medId;
        this.frequency = frequency;
        this.startDate= startDate;
        this.endDate = endDate;
        this.days = days;
        this.starttime = starttime;
        this.duration = duration;
        this.month = month;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setMedId(long medId) {
        this.medId = medId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public long getMedId() {
        return medId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getDays() {
        return days;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
