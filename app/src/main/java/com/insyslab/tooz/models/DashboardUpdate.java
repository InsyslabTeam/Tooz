package com.insyslab.tooz.models;

/**
 * Created by TaNMay on 12/10/17.
 */

public class DashboardUpdate {

    private Boolean isContactUpdate = false;

    public DashboardUpdate(Boolean isContactUpdate) {
        this.isContactUpdate = isContactUpdate;
    }

    public Boolean isContactUpdate() {
        return isContactUpdate;
    }
}
