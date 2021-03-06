package com.insyslab.tooz.restclient;

import com.insyslab.tooz.models.responses.Error;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;

public interface SuccessListener {

    void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString, Type responseType);

    void onError(Error error);
}
