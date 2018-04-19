package com.insyslab.tooz.interfaces;

import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.UserGroup;

/**
 * Created by TaNMay on 28/03/18.
 */

public interface OnUserFetchedListener {

    void onUserFetched(User user);

    void onGroupFetched(UserGroup group);
}
