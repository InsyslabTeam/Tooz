package com.insyslab.tooz.interfaces;

import android.view.View;

/**
 * Created by TaNMay on 28/11/17.
 */

public interface OnPastReminderClickListener {

    void onPastReminderClick(View view);

    void onPastReminderEditClick(int position);

    void onPastReminderDeleteClick(int position);

}
