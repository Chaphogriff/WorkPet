package com.workthrutheweak.workpet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

import java.sql.Date;
import java.sql.Time;

//Class for a Task item for recycler view
public class Task implements Parcelable {
    //Task Title string ID
    public int titleId;
    //Task Description string ID
    public int descriptionId;
    //Task Date string ID
    public int dateId;
    //Task Date string ID
    public int rewardId;
    @SerializedName("Title")
    public String _title;

    public int getTitleId() {
        return titleId;
    }

    public int getDescriptionId() {
        return descriptionId;
    }

    public int getDateId() {
        return dateId;
    }

    public int getRewardId() {
        return rewardId;
    }

    public String get_title() {
        return _title;
    }

    public String get_description() {
        return _description;
    }

    public String get_date() {
        return _date;
    }

    public String get_reward() {
        return _reward;
    }

    public boolean isTaskDone() {
        return isTaskDone;
    }

    @SerializedName("Description")
    public String _description;
    @SerializedName("Date")
    public String _date;
    @SerializedName("Reward")
    public String _reward;
    //Task boolean to check if Task is done for checkbox
    public boolean isTaskDone = false;

    /*public Task(int titleId, int descriptionId, int dateId, int rewardId){
        this.titleId = titleId;
        this.descriptionId = descriptionId;
        this.dateId = dateId;
        this.rewardId = rewardId;
    }*/

    public Task(String title, String Desc, String Date, String Time, String Gold, String xp) {
        this._title = title;
        this._description = Desc;
        this._date = Date + " " + Time;
        this._reward = Gold + " gold and " + xp + " xp";
    }

    public Task(String title, String Desc, String Date, String Reward){
        this._title = title;
        this._date = Date;
        this._description = Desc;
        this._reward = Reward;
    }

    protected Task(Parcel in) {
        titleId = in.readInt();
        descriptionId = in.readInt();
        dateId = in.readInt();
        rewardId = in.readInt();
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
        parcel.writeInt(titleId);
        parcel.writeInt(descriptionId);
        parcel.writeInt(dateId);
        parcel.writeInt(rewardId);
        parcel.writeString(_title);
        parcel.writeString(_description);
        parcel.writeString(_date);
        parcel.writeString(_reward);
        parcel.writeByte((byte) (isTaskDone ? 1 : 0));
    }
}
