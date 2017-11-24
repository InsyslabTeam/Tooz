package com.insyslab.tooz.utils;

import static com.insyslab.tooz.utils.ConstantClass.IS_DEV;

/**
 * Created by TaNMay on 15/02/17.
 */

public class AppConstants {

    public static final String BASE_DEV_URL = "";
    public static final String BASE_PROD_URL = "";

    public static final String BASE_URL = IS_DEV ? BASE_DEV_URL : BASE_PROD_URL;

    public static final String KEY_USER_SHARED_PREFERENCE = "UserSharedPrefs";
    public static final String KEY_TOOZ_SHARED_PREFERENCE = "TOOZSharedPrefs";

    public static final String KEY_SHARED_PREFS_TOKEN = "TOKEN";
    public static final String KEY_SHARED_PREFS_USER = "USER";

    public static final String KEY_TO_ONBOARDING = "KEY_TO_ONBOARDING";

    public static final String KEY_OTP_SMS = "otp";
    public static final String KEY_OTP_MESSAGE = "KEY_OTP_MESSAGE";
    public static final String KEY_OTP_NUMBER = "KEY_OTP_NUMBER";
    public static final String KEY_SMS_PDUS = "pdus";

    public static final String VAL_OTP_NUMBER = "MD-SMSMsg";

    public static final int ERROR_CODE_UNAUTH = 422;
    public static final int ERROR_CODE_SERVER = 500;

    public static final String KEY_PUSH_NOTIFICATION = "KEY_PUSH_NOTIFICATION";
    public static final String KEY_PUSH_MESSAGE = "message";
}
