package com.insyslab.tooz.restclient;

import com.google.gson.Gson;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.requests.ContactSyncRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public RequestBuilder() {
    }

    public JSONObject getSignInRequestPayload(String mobile) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key_mobile, mobile);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getVerifyOtpRequestPayload(String mobile, String otp) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key_mobile, mobile);
            jsonObject.put(key_otp, otp);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getResendOtpRequestPayload(String mobile) {
        return getSignInRequestPayload(mobile);
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
                                                      String latitude, String longitude,
                                                      String ownerUser, JSONArray contactsArray) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(key_task, task);
            jsonObject.put(key_owner_user, ownerUser);
            if (dateTime != null) {
                jsonObject.put(key_time, dateTime);
                jsonObject.put(key_date, dateTime);
            }
            if (latitude != null) jsonObject.put(key_latitude, latitude);
            if (longitude != null) jsonObject.put(key_longitude, longitude);

            if (type.equals(VAL_SEND_REMINDER)) {
                jsonObject.put(key_contacts, contactsArray);
            }
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
