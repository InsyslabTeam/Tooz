package com.insyslab.tooz.restclient;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.utils.LocalStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

import static com.insyslab.tooz.utils.ConstantClass.BASE_URL;
import static com.insyslab.tooz.utils.Util.DEFAULT_DATE_FORMAT;

/**
 * Created by TaNMay on 06/10/17.
 */

public class GenericDataHandler implements SuccessListener {

    private BaseResponseInterface baseResponseInterface;
    private Context context;
    private int requestCode;
    private String token = null;

    public GenericDataHandler(BaseResponseInterface baseResponseInterface, Context context, int requestCode) {
        this.baseResponseInterface = baseResponseInterface;
        this.context = context;
        this.requestCode = requestCode;

        token = LocalStorage.getInstance(context).getToken();
    }

    public void jsonObjectRequest(JSONObject request, String url, Integer method, Type responseType) {
        HttpRequestHandler.makeJsonObjectRequest(request, BASE_URL + url, token, method, this, responseType, context);
    }

    public void jsonArrayRequest(String url, Type responseType) {
        HttpRequestHandler.makeJsonArrayRequest(BASE_URL + url, token, this, responseType, context);
    }

    public void customRequest(JSONObject request, String url, Integer method, Type responseType) {
        HttpRequestHandler.makeCustomRequest(request, BASE_URL + url, token, method, this, responseType, context);
    }

    public void stringRequest(String url, Integer method) {
        HttpRequestHandler.makeStringRequest(BASE_URL + url, token, method, this, context);
    }

    public void multipartRequest(String url, Integer method, Map<String, VolleyMultipartRequest.DataPart> partMap, Map<String, String> paramsMap, Type responseType) {
        HttpRequestHandler.makeMultipartRequest(BASE_URL + url, token, method, this, partMap, paramsMap, responseType, context, responseType == String.class);
    }

    @Override
    public void onSuccess(JSONObject responseObject, JSONArray responseArray, String responseString, Type responseType) {
        if (responseObject == null && responseArray == null && responseString != null) {
            baseResponseInterface.onResponse(responseString, null, requestCode);
        } else if (responseObject == null && responseArray != null && responseString == null) {
            Gson gson = new GsonBuilder().setDateFormat(DEFAULT_DATE_FORMAT).create();
            Object response = gson.fromJson(responseArray.toString(), responseType);
            baseResponseInterface.onResponse(response, null, requestCode);
        } else if (responseObject != null && responseArray == null && responseString == null) {
            Object response = new Gson().fromJson(responseObject.toString(), responseType);
            baseResponseInterface.onResponse(response, null, requestCode);
        } else {
            baseResponseInterface.onResponse(null, new Error("Response handling error.", -100), requestCode);
        }
    }

    @Override
    public void onError(Error error) {
        baseResponseInterface.onResponse(null, error, requestCode);
    }
}