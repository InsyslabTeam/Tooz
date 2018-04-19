package com.insyslab.tooz.restclient;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.UserGroup;
import com.insyslab.tooz.models.requests.ContactSyncRequest;
import com.insyslab.tooz.models.requests.CreateGroupRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.insyslab.tooz.utils.AppConstants.VAL_SEND_REMINDER;

/**
 * Created by TaNMay on 24/11/17.
 */

public class RequestBuilder {

    private final String key_mobile = "mobile";
    private final String key_otp = "otp";
    private final String key_name = "name";

    private final String key_task = "task";
    private final String key_date = "date";
    private final String key_time = "time";
    private final String key_latitude = "latitude";
    private final String key_longitude = "longitude";
    private final String key_owner_user = "user";
    private final String key_contacts = "contacts";
    private final String key_feedback = "feedback";
    private final String key_device_id = "deviceId";

    public RequestBuilder() {
    }

    public JSONObject getSignInRequestPayload(String mobile, String deviceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key_mobile, mobile);
            if (deviceId != null) jsonObject.put(key_device_id, deviceId);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getVerifyOtpRequestPayload(String mobile, String otp, String deviceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key_mobile, mobile);
            jsonObject.put(key_otp, otp);
            if (deviceId != null) jsonObject.put(key_device_id, deviceId);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getResendOtpRequestPayload(String mobile, String deviceId) {
        return getSignInRequestPayload(mobile, deviceId);
    }

    public JSONObject getCreateProfileRequestPayload(String name, User user) {
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(user));
            jsonObject.put(key_name, name);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public JSONObject getContactSyncRequestPayload(ContactSyncRequest contactSyncRequest) {
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(contactSyncRequest));
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getCreateReminderRequestPayload(String type, String task, String dateTime,
                                                      LatLng location, String ownerUser,
                                                      List<User> contactsArray, List<UserGroup> groupsArray) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(key_task, task);
            jsonObject.put(key_owner_user, ownerUser);
            if (dateTime != null) {
                jsonObject.put(key_time, dateTime);
                jsonObject.put(key_date, dateTime);
            }
            if (location != null) jsonObject.put(key_latitude, location.latitude);
            if (location != null) jsonObject.put(key_longitude, location.longitude);

            if (type.equals(VAL_SEND_REMINDER)) {
                jsonObject.put(key_contacts, getUserIdList(contactsArray, groupsArray));
            } else {
                jsonObject.put(key_contacts, new JSONArray());
            }
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONArray getUserIdList(List<User> contactsArray, List<UserGroup> groupsArray) {
        JSONArray jsonArray = new JSONArray();
        if (contactsArray != null && groupsArray != null && contactsArray.size() > groupsArray.size()) {
            for (int i = 0; i < contactsArray.size(); i++) {
                jsonArray.put(contactsArray.get(i).getId());
            }
        } else if (contactsArray != null && groupsArray != null && contactsArray.size() < groupsArray.size()) {
            for (int i = 0; i < groupsArray.size(); i++) {
                jsonArray.put(groupsArray.get(i).getId());
            }
        }
        return jsonArray;
    }

    public JSONObject getCreateGroupRequestPayload(String groupName, List<User> selectedMembers, String imageUrl, String createrId) {
        try {
            CreateGroupRequest createGroupRequest = new CreateGroupRequest();

            createGroupRequest.setName(groupName);
            createGroupRequest.setGroupCreator(createrId);

            List<String> members = new ArrayList<>();
            for (int i = 0; i < selectedMembers.size(); i++) {
                members.add(selectedMembers.get(i).getId());
            }

            createGroupRequest.setUsers(members);
            createGroupRequest.setUrl(imageUrl);

            JSONObject jsonObject = new JSONObject(new Gson().toJson(createGroupRequest));
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getFeedbackRequestPayload(String feedbackText) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(key_feedback, feedbackText);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getInviteRequestPayload(String mobile) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(key_mobile, mobile);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
