package com.insyslab.tooz.models.eventbus;

/**
 * Created by TaNMay on 12/10/17.
 */

public class ReminderCreated {

    private Boolean isReminderCreated = false;

    private Boolean isPersonalReminder = true;

    public ReminderCreated(Boolean isReminderCreated, Boolean isPersonalReminder) {
        this.isReminderCreated = isReminderCreated;
        this.isPersonalReminder = isPersonalReminder;
    }

    public Boolean isReminderCreated() {
        return isReminderCreated;
    }

    public Boolean isPersonalReminder() {
        return isPersonalReminder;
    }
}
