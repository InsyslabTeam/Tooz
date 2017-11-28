package com.insyslab.tooz.models;

import android.net.Uri;

/**
 * Created by TaNMay on 24/11/17.
 */

public class PhoneContact {

    private String name;

    private String phoneNumber;

    private Uri contactImageUri;

    private Boolean isSelected = false;

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
}
