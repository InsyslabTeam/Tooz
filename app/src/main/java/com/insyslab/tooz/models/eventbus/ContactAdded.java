package com.insyslab.tooz.models.eventbus;

/**
 * Created by TaNMay on 12/10/17.
 */

public class ContactAdded {

    private Boolean isContactAdded = false;

    public ContactAdded(Boolean isContactAdded) {
        this.isContactAdded = isContactAdded;
    }

    public Boolean isContactAdded() {
        return isContactAdded;
    }
}
