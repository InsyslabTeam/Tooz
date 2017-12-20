package com.insyslab.tooz.restclient;

import com.google.gson.Gson;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.requests.ContactSyncRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TaNMay on 24/11/17.
 */

public class RequestBuilder {

    private final String key_mobile = "mobile";
    private final String key_otp = "otp";
    private final String key_name = "name";

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

    public JSONObject getContactSyncRequest(ContactSyncRequest contactSyncRequest) {
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(contactSyncRequest));
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
