package com.insyslab.tooz.models.eventbus;

/**
 * Created by TaNMay on 12/10/17.
 */

public class ContactSyncUpdate {

    private Boolean isContactSynced = false;

    public ContactSyncUpdate(Boolean isContactSynced) {
        this.isContactSynced = isContactSynced;
    }

    public Boolean isContactSynced() {
        return isContactSynced;
    }
}
