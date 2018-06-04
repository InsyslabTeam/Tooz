package com.insyslab.tooz.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.insyslab.tooz.models.responses.Token;
import com.insyslab.tooz.rpl.UserListConverter;

import java.util.ArrayList;

@Entity(indices = {@Index(value = "id", unique = true)})
@TypeConverters({UserListConverter.class})
public class UserGroup {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "p_id")
    public long pId;

    @ColumnInfo(name = "mobile")
    @SerializedName("mobile")
    @Expose
    private String mobile;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("isVerified")
    @Expose
    private Boolean isVerified;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("contactsSync")
    @Expose
    private Boolean contactsSync;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    @SerializedName("otp")
    @Expose
    private String otp;

    @ColumnInfo(name = "profileImage")
    @SerializedName("profileImage")
    @Expose
    private String profileImage;

    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("active")
    @Expose
    private Boolean active;

    @Ignore
    @SerializedName("token")
    @Expose
    private Token token;

    private Boolean isBlocked = false;

    private Boolean isSelected = false;

    @ColumnInfo(name = "users")
    @TypeConverters({UserListConverter.class})
    @SerializedName("users")
    @Expose
    private ArrayList<User> users = new ArrayList<>();

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getContactsSync() {
        return contactsSync;
    }

    public void setContactsSync(Boolean contactsSync) {
        this.contactsSync = contactsSync;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}

