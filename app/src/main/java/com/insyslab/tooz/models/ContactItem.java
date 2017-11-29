package com.insyslab.tooz.models;

/**
 * Created by TaNMay on 28/11/17.
 */

public class ContactItem {

    private String name;

    private String number;

    private Boolean isBlocked = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }
}
