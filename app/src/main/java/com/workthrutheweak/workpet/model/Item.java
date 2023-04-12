package com.workthrutheweak.workpet.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

//Class for a Task item for recycler view
public class Item implements Serializable {
    private String ItemId;
    @SerializedName("Title")
    private String title;
    @SerializedName("Description")
    private String description;
    @SerializedName("Price")
    private int price;
    @SerializedName("Effect")
    private int effect;

    public Item(){};

    @SuppressLint("NewApi")
    public Item(String _title, String _description,int _price, int _effect) {
        this.title = _title;
        this.description = _description;
        this.price = _price;
        this.effect = _effect;
    }

    @Exclude

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getEffect() {
        return effect;
    }

    public void setEffect(int effect) {
        this.effect = effect;
    }
}
