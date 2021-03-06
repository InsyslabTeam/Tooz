package com.insyslab.tooz.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.insyslab.tooz.models.User;

import java.util.List;

public class GetContactsResponse {

    @SerializedName("appUser")
    @Expose
    private List<User> appUser = null;

    @SerializedName("nonAppUser")
    @Expose
    private List<User> nonAppUser = null;

    public List<User> getAppUser() {
        return appUser;
    }

    public void setAppUser(List<User> appUser) {
        this.appUser = appUser;
    }

    public List<User> getNonAppUser() {
        return nonAppUser;
    }

    public void setNonAppUser(List<User> nonAppUser) {
        this.nonAppUser = nonAppUser;
    }

}
