package com.insyslab.tooz.models.eventbus;

/**
 * Created by TaNMay on 12/10/17.
 */

public class GroupCreated {

    private Boolean isGroupCreated = false;

    public GroupCreated(Boolean isGroupCreated) {
        this.isGroupCreated = isGroupCreated;
    }

    public Boolean isGroupCreated() {
        return isGroupCreated;
    }
}
