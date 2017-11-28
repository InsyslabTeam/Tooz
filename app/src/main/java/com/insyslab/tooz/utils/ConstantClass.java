package com.insyslab.tooz.utils;

/**
 * Created by TaNMay on 28/09/16.
 */
public class ConstantClass {

    public static final boolean IS_DEV = true;

    public static final String APP_NAME = "Tooz";

    public static final int SPLASH_TIME_OUT = 2000;

    public static final String BASE_DEV_URL = "";
    public static final String BASE_PROD_URL = "";

    public static final String BASE_URL = IS_DEV ? BASE_DEV_URL : BASE_PROD_URL;

    public static final int REQUEST_TYPE_001 = 1001;
    public static final String SIGN_IN_REQUEST_URL = "";

    public static final int REQUEST_TYPE_002 = 1002;
    public static final String VERIFY_OTP_REQUEST_URL = "";

    public static final int REQUEST_TYPE_003 = 1003;
    public static final String RESEND_OTP_REQUEST_URL = "";

    public static final int REQUEST_TYPE_004 = 1004;
    public static final String CREATE_PROFILE_REQUEST_URL = "";

    public static final int REQUEST_TYPE_005 = 1005;
    public static final String CONTACTS_SYNC_REQUEST_URL = "";
}
