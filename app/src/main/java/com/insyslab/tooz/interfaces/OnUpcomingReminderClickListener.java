package com.insyslab.tooz.interfaces;

import android.view.View;

public interface OnUpcomingReminderClickListener {

    void onUpcomingReminderClick(View view);

    void onUpcomingReminderEditClick(int position);

    void onUpcomingReminderDeleteClick(int position);
}
