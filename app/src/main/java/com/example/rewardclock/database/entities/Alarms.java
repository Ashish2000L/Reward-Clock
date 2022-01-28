package com.example.rewardclock.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Alarms {

    public Alarms(int id, long timestamp, String hour, String minute, String message,Boolean ampm, Boolean onoff, Boolean everyday) {
        this.id = id;
        this.timestamp = timestamp;
        this.hour = hour;
        this.minute = minute;
        this.message = message;
        this.ampm = ampm;
        this.onoff = onoff;
        this.everyday = everyday;
//        this.is_snooze =is_snooze;
    }

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "hour")
    public String hour;

    @ColumnInfo(name = "minute")
    public String minute;

    @ColumnInfo(name = "ampm")
    public Boolean ampm;

    @ColumnInfo(name = "onoff")
    public Boolean onoff;

    @ColumnInfo(name = "snooze")
    public Boolean is_snooze;

    @ColumnInfo(name = "everyday")
    public Boolean everyday;

    @ColumnInfo(name = "message")
    public String message;

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getAmpm() {
        return ampm;
    }

    public void setAmpm(Boolean ampm) {
        this.ampm = ampm;
    }

    public Boolean getOnoff() {
        return onoff;
    }

    public void setOnoff(Boolean onoff) {
        this.onoff = onoff;
    }

    public Boolean getEveryday() {
        return everyday;
    }

    public void setEveryday(Boolean everyday) {
        this.everyday = everyday;
    }

    public Boolean getIs_snooze() {
        return is_snooze;
    }

    public void setIs_snooze(Boolean is_snooze) {
        this.is_snooze = is_snooze;
    }
}
