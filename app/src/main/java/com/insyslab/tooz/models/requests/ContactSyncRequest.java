package com.insyslab.tooz.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContactSyncRequest {

    @SerializedName("contacts")
    @Expose
    private List<Contact_> contacts = null;

    public List<Contact_> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact_> contacts) {
        this.contacts = contacts;
    }

}