package com.insyslab.tooz.models.eventbus;

public class ContactAdded {

    private Boolean isContactAdded = false;

    public ContactAdded(Boolean isContactAdded) {
        this.isContactAdded = isContactAdded;
    }

    public Boolean isContactAdded() {
        return isContactAdded;
    }
}
