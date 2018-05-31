package com.insyslab.tooz.utils;

public class ConstantClass {

    static final boolean IS_DEV = true;

//    public static final String APP_NAME = "Tooz";

    public static final int SPLASH_TIME_OUT = 2000;

    private static final String BASE_DEV_URL = "http://35.154.234.106:1337/";
    private static final String BASE_PROD_URL = "";

    public static final String BASE_URL = IS_DEV ? BASE_DEV_URL : BASE_PROD_URL;

    public static final String API_KEY = "$2a$10$.CPkZU3.R3bRE3bDI5epUuuHrUe63EbqW7HhhOriSj5beFwTdT16W";

    public static final String DEFAULT_APP_SHARE_TEXT = "Tooz is your personal assistant, to remind " +
            "you about anything and everything, anywhere and everywhere, anytime and everytime....from people you like!";
    public static final String APP_URL = "https://play.google.com/store/apps/details?id=com.insyslab.tooz";

    /**
     * USER
     */
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

//    public static final int REQUEST_TYPE_023 = 1023;
//    public static final String UPDATE_USER_PREFERENCES_REQUEST_URL = "userpreference/updateUserPreference";

    /**
     * REMINDER
     */
    public static final int REQUEST_TYPE_009 = 1009;
    public static final String CREATE_REMINDER_REQUEST_URL = "reminder/addReminder";

    public static final int REQUEST_TYPE_010 = 1010;
    public static final String UPDATE_REMINDER_REQUEST_URL = "reminder/updateReminder";

    public static final int REQUEST_TYPE_011 = 1011;
    public static final String GET_ALL_REMINDERS_REQUEST_URL = "reminder/getAllReminders";

    public static final int REQUEST_TYPE_012 = 1012;
    public static final String DELETE_REMINDER_REQUEST_URL = "reminder/deleteReminder/";

    /**
     * GROUPS
     */
    public static final int REQUEST_TYPE_013 = 1013;
    public static final String CREATE_GROUP_REQUEST_URL = "group/addGroup";

//    public static final int REQUEST_TYPE_014 = 1014;
//    public static final String UPDATE_GROUP_REQUEST_URL = "group/updateGroup";

//    public static final int REQUEST_TYPE_015 = 1015;
//    public static final String DELETE_GROUP_REQUEST_URL = "group/deleteGroup/";

    public static final int REQUEST_TYPE_016 = 1016;
    public static final String GET_MY_GROUPS_REQUEST_URL = "group/getMyGroups";

    public static final int REQUEST_TYPE_017 = 1017;
    public static final String GROUP_PICTURE_REQUEST_URL = "group/groupProfile";

    /**
     * BLOCK/UNBLOCK
     */
    public static final int REQUEST_TYPE_018 = 1018;
    public static final String BLOCK_CONTACT_REQUEST_URL = "user/blockUser";

    public static final int REQUEST_TYPE_019 = 1019;
    public static final String GET_BLOCKED_CONTACTS_REQUEST_URL = "user/getBlockedUser";

    /**
     * OTHERS
     */
//    public static final int REQUEST_TYPE_020 = 1020;
//    public static final String DELETE_ACCOUNT_REQUEST_URL = "";

    public static final int REQUEST_TYPE_021 = 1021;
    public static final String SEND_FEEDBACK_REQUEST_URL = "user/feedBack";

    public static final int REQUEST_TYPE_022 = 1022;
    public static final String INVITE_NON_APP_USER_REQUEST_URL = "";

    // REQUEST TYPE 23 = defined above (line 52)

//    public static final int REQUEST_TYPE_024 = 1024;
//    public static final String _REQUEST_URL = "";
}
