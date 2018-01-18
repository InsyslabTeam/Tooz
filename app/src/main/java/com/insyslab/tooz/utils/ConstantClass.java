package com.insyslab.tooz.utils;

/**
 * Created by TaNMay on 28/09/16.
 */
public class ConstantClass {

    public static final boolean IS_DEV = true;

    public static final String APP_NAME = "Tooz";

    public static final int SPLASH_TIME_OUT = 2000;

    public static final String BASE_DEV_URL = "http://35.154.234.106:1337/";
    public static final String BASE_PROD_URL = "";

    public static final String BASE_URL = IS_DEV ? BASE_DEV_URL : BASE_PROD_URL;

    public static final String API_KEY = "$2a$10$.CPkZU3.R3bRE3bDI5epUuuHrUe63EbqW7HhhOriSj5beFwTdT16W";

    public static final String DEFAULT_APP_SHARE_TEXT = "Tooz is your personal assistant, to remind " +
            "you about anything and everything, anywhere and everywhere, anytime and everytime....from people you like!";
    public static final String APP_URL = "https://play.google.com/store/apps/details?id=com.insyslab.tooz";

    public static final int REQUEST_TYPE_001 = 1001;
    public static final String SIGN_IN_REQUEST_URL = "user/signup";

    public static final int REQUEST_TYPE_002 = 1002;
    public static final String VERIFY_OTP_REQUEST_URL = "user/verifyOTP";

    public static final int REQUEST_TYPE_003 = 1003;
    public static final String RESEND_OTP_REQUEST_URL = SIGN_IN_REQUEST_URL;

    public static final int REQUEST_TYPE_004 = 1004;
    public static final String CREATE_PROFILE_REQUEST_URL = "user/updateProfile";

    public static final int REQUEST_TYPE_005 = 1005;
    public static final String CONTACTS_SYNC_REQUEST_URL = "user/contactsync";

    public static final int REQUEST_TYPE_006 = 1006;
    public static final String GET_CONTACTS_REQUEST_URL = "user/getContacts";

    public static final int REQUEST_TYPE_007 = 1007;
    public static final String LOGOUT_REQUEST_URL = "user/logout";

    public static final int REQUEST_TYPE_008 = 1008;
    public static final String UPDATE_PROFILE_PICTURE_REQUEST_URL = "user/imageProfile";

    public static final int REQUEST_TYPE_009 = 1009;
    public static final String CREATE_REMINDER_REQUEST_URL = "reminder/addReminder";

    public static final int REQUEST_TYPE_010 = 1010;
    public static final String UPDATE_REMINDER_REQUEST_URL = "reminder/updateReminder";

    public static final int REQUEST_TYPE_011 = 1011;
    public static final String GET_ALL_REMINDERS_REQUEST_URL = "reminder/getAllReminders";
}
