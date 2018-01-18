package com.insyslab.tooz.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(indices = {@Index(value = "id", unique = true)})
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "p_id")
    public long pId;

    @Ignore
    @SerializedName("contacts")
    @Expose
    private List<User> contacts = null;

    @Embedded
    @SerializedName("user")
    @Expose
    private User_ user;

    @ColumnInfo(name = "task")
    @SerializedName("task")
    @Expose
    private String task;

    @ColumnInfo(name = "date")
    @SerializedName("date")
    @Expose
    private String date;

    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    @Expose
    private String longitude;

    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    private String id;

    private Boolean isExpanded = false;

    public Boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }

    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }

    public User_ getUser() {
        return user;
    }

    public void setUser(User_ user) {
        this.user = user;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}