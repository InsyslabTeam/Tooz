package com.insyslab.tooz.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.insyslab.tooz.rpl.TimestampConverter;

import java.util.Date;

@Entity(indices = {@Index(value = "id", unique = true)})
@TypeConverters(TimestampConverter.class)
public class LocalReminder {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "p_id")
    public Integer pId;

    @ColumnInfo(name = "task")
    @SerializedName("task")
    @Expose
    private String task;

    @ColumnInfo(name = "date")
    @TypeConverters({TimestampConverter.class})
    @SerializedName("date")
    @Expose
    private Date date;

    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    @Expose
    private String longitude;

    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    @Expose
    private String latitude;

    @ColumnInfo(name = "createdAt")
    @TypeConverters({TimestampConverter.class})
    @SerializedName("createdAt")
    @Expose
    private Date createdAt;

    @ColumnInfo(name = "updatedAt")
    @TypeConverters({TimestampConverter.class})
    @SerializedName("updatedAt")
    @Expose
    private Date updatedAt;

    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    private String id;

    @ColumnInfo(name = "uniqueInt")
    @SerializedName("uniqueInt")
    @Expose
    private Integer uniqueInt;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUniqueInt() {
        return uniqueInt;
    }

    public void setUniqueInt(Integer uniqueInt) {
        this.uniqueInt = uniqueInt;
    }
}