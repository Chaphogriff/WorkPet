package com.workthrutheweak.workpet.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

//Class for a Task item for recycler view
public class Task implements Parcelable {
    @SerializedName("Title")
    public String _title;
    @SerializedName("Description")
    public String _description;
    @SerializedName("Date")
    public String _date;
    @SerializedName("Reward")
    public String _reward;
    public LocalDate localDate;
    public LocalTime localTime;
    @SerializedName("Gold")
    public int goldreward;
    @SerializedName("XP")
    public int xpreward;
    //Task boolean to check if Task is done for checkbox
    @SerializedName("isTaskDone")
    public boolean isTaskDone = false;

    List<String> TaskString;

    @SuppressLint("NewApi")
    public Task(String _title, String _description, LocalDate localDate, LocalTime localTime, int goldreward, int xpreward, boolean isTaskDone) {
        this._title = _title;
        this._description = _description;
        this.localDate = localDate;
        this.localTime = localTime;
        this.goldreward = goldreward;
        this.xpreward = xpreward;
        this.isTaskDone = isTaskDone;
    }

    public List<String> inString() {
        List<String> list = new ArrayList<>();
        list.add(this._title);
        list.add(this._description);
        list.add(this.localDate.toString() + " " + this.localTime.toString());
        list.add(this.goldreward + " gold, " + this.xpreward + " xp");
        return list;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public int getGoldreward() {
        return goldreward;
    }

    public int getXpreward() {
        return xpreward;
    }


    public String get_title() {
        return _title;
    }

    public String get_description() {
        return _description;
    }

    public boolean isTaskDone() {
        return isTaskDone;
    }

    public Task(String title, String Desc, String Date, String Time, String Gold, String xp, boolean isTaskDone) {
        this._title = title;
        this._description = Desc;
        this._date = Date + " " + Time;
        this._reward = Gold + " gold and " + xp + " xp";
        this.isTaskDone = isTaskDone;
    }

    public Task(String title, String Desc, String Date, String Reward, boolean isTaskDone) {
        this._title = title;
        this._date = Date;
        this._description = Desc;
        this._reward = Reward;
        this.isTaskDone = isTaskDone;
    }

    protected Task(Parcel in) {
        _title = in.readString();
        _description = in.readString();
        _date = in.readString();
        _reward = in.readString();
        isTaskDone = in.readByte() != 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(_title);
        parcel.writeString(_description);
        parcel.writeString(_date);
        parcel.writeString(_reward);
        parcel.writeByte((byte) (isTaskDone ? 1 : 0));
    }
}
