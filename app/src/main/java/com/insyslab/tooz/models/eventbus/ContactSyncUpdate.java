package com.insyslab.tooz.models.eventbus;

public class ContactSyncUpdate {

    private Boolean isContactSynced = false;

    public ContactSyncUpdate(Boolean isContactSynced) {
        this.isContactSynced = isContactSynced;
    }

    public Boolean isContactSynced() {
        return isContactSynced;
    }
}
