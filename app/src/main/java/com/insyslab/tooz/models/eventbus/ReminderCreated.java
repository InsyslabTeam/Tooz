package com.insyslab.tooz.models.eventbus;

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
