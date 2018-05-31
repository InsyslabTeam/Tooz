package com.insyslab.tooz.interfaces;

import android.view.View;

public interface OnUserContactClickListener {

//    void onAppUserContactClick(View view);

    void onNonAppUserContactClick(View view);

    void onNonAppUserInviteClick(int position);

    void onAppUserSendReminderClick(int position);

    void onAppUserBlockClick(int position);

    void onMyGroupsSendReminderClick(int position);
}
