package com.insyslab.tooz.interfaces;

import android.view.View;

/**
 * Created by TaNMay on 28/11/17.
 */

public interface OnUserContactClickListener {

    void onAppUserContactClick(View view);

    void onNonAppUserContactClick(View view);

    void onNonAppUserInviteClick(int position);

    void onAppUserSendReminderClick(int position);

    void onAppUserBlockClick(int position);
}
