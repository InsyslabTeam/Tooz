package com.insyslab.tooz.restclient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TaNMay on 24/11/17.
 */

public class RequestBuilder {

    private final String key_mobile_number = "mobile_number";

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
}
