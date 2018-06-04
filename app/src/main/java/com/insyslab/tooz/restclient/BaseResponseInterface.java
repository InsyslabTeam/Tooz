package com.insyslab.tooz.restclient;

public interface BaseResponseInterface {

    void onResponse(Object success, Object error, int requestCode);
}
