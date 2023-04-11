package com.workthrutheweak.workpet.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

//Class for a Task item for recycler view
public class Task {
    private String TaskId;
    @SerializedName("Title")
    private String title;
    @SerializedName("Description")
    private String description;
    //public LocalDate localDate;
    @SerializedName("Year")
    private int year;
    @SerializedName("Month")
    private int month;
    @SerializedName("Day")
    private int day;
    //private LocalTime localTime;
    @SerializedName("Hour")
    private int hour;
    @SerializedName("Minute")
    private int minute;
    @SerializedName("Gold")
    private int goldreward;
    @SerializedName("XP")
    private int xpreward;
    @SerializedName("isTaskDone")
    private boolean isTaskDone = false;
    @SerializedName("Mode")
    private String mode = "Once";

    List<String> TaskString;

    public Task(){};
    @SuppressLint("NewApi")
    public Task(String _title, String _description,int _year, int _month, int _day, int _hour,int _minute,
                int _goldreward, int _xpreward, boolean _isTaskDone, String _mode) {
        this.title = _title;
        this.description = _description;
        this.year = _year;
        this.month = _month;
        this.day = _day;
        //this.localDate = LocalDate.of(this.year,this.month,this.day);
        this.hour = _hour;
        this.minute = _minute;
        //this.localTime = LocalTime.of(this.hour,this.minute);
        this.goldreward = _goldreward;
        this.xpreward = _xpreward;
        this.isTaskDone = _isTaskDone;
        this.mode = _mode;
    }
    public List<String> inString() {
        List<String> list = new ArrayList<>();
        list.add(this.title);
        list.add(this.description);
        list.add(this.day + "/" + this.month + "/"+ this.year + " at " + this.hour+":"+this.minute);
        list.add(this.goldreward + " gold, " + this.xpreward + " xp");
        return list;
    }

    @Exclude
    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getGoldreward() {
        return goldreward;
    }

    public void setGoldreward(int goldreward) {
        this.goldreward = goldreward;
    }

    public int getXpreward() {
        return xpreward;
    }

    public void setXpreward(int xpreward) {
        this.xpreward = xpreward;
    }

    public boolean isTaskDone() {
        return isTaskDone;
    }

    public void setTaskDone(boolean taskDone) {
        isTaskDone = taskDone;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
