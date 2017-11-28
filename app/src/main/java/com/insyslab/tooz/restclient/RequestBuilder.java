package com.insyslab.tooz.restclient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TaNMay on 24/11/17.
 */

public class RequestBuilder {

    private final String key_mobile_number = "mobile_number";
    private final String key_name = "name";
    private JSONObject contactSyncRequest;

    public RequestBuilder() {
    }

    public JSONObject getSignInRequestPayload(String mobile_number) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key_mobile_number, mobile_number);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getVerifyOtpRequestPayload() {
        return null;
    }

    public JSONObject getResendOtpRequestPayload() {
        return null;
    }

    public JSONObject getCreateProfileRequestPayload(String name) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key_name, name);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public JSONObject getContactSyncRequest() {
        return null;
    }
}
