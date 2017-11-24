package com.insyslab.tooz.restclient;

/**
 * Created by TaNMay on 06/10/17.
 */

public interface BaseResponseInterface {

    void onResponse(Object success, Object error, int requestCode);
}
