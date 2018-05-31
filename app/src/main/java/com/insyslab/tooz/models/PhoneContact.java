package com.insyslab.tooz.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;

@Entity(indices = {@Index(value = "p_id", unique = true)})
public class PhoneContact {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "p_id")
    public long pId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "phoneNumber")
    private String phoneNumber;

    @Ignore
    private Uri contactImageUri;

    private Boolean isSelected = false;

    @ColumnInfo(name = "isSynced")
    private Boolean isSynced = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Uri getContactImageUri() {
        return contactImageUri;
    }

    public void setContactImageUri(Uri contactImageUri) {
        this.contactImageUri = contactImageUri;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Boolean isSynced() {
        return isSynced;
    }

    public void setSynced(Boolean synced) {
        isSynced = synced;
    }
}
