package com.insyslab.tooz.models.eventbus;

public class GroupCreated {

    private Boolean isGroupCreated = false;

    public GroupCreated(Boolean isGroupCreated) {
        this.isGroupCreated = isGroupCreated;
    }

    public Boolean isGroupCreated() {
        return isGroupCreated;
    }
}
