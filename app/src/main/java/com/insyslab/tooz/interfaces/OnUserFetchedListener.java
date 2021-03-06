package com.insyslab.tooz.interfaces;

import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.UserGroup;

public interface OnUserFetchedListener {

    void onUserFetched(User user);

    void onGroupFetched(UserGroup group);
}
