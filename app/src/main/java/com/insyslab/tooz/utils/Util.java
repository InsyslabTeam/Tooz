package com.insyslab.tooz.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.insyslab.tooz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by TaNMay on 21/02/17.
 */

public class Util {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String REQUIRED_DATE_FORMAT = "dd-MM-yyyy";

    private static ConnectivityManager mCM;

    private final String TAG = "Util ==>";

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = activity.getCurrentFocus();
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static boolean hasInternetAccess(Context context) {
        if (mCM == null)
            mCM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mCM.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String getDateFromGmtToIst(String gmtDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        SimpleDateFormat output = new SimpleDateFormat(REQUIRED_DATE_FORMAT);
        Date date = null;
        try {
            date = sdf.parse(gmtDate);
            sdf.setTimeZone(TimeZone.getTimeZone("IST"));
            return output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDayOfWeekFromIndex(Integer intDay) {
        return new DateFormatSymbols().getWeekdays()[intDay - 1].substring(0, 3).toUpperCase();
    }

    public static String getAmPmFromIndex(Integer intMeridian) {
        return intMeridian == 0 ? "AM" : "PM";
    }

    public static String getMonthFromIndex(Integer intMonth) {
        return new DateFormatSymbols().getMonths()[intMonth - 1].substring(0, 3);
    }

    public static String getDateExtension(Integer intDate) {
        String dateStr = intDate.toString();
        if (dateStr.endsWith("1")) return "st";
        else if (dateStr.endsWith("2")) return "nd";
        else return "th";
    }

    public static boolean verifyPermission(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static String getDeviceId() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    public static long getTimeInMilliSeconds(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static JSONObject getJsonObjectFromString(String objectString) {
        try {
            return new JSONObject(objectString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getToTwoDecimalPlaces(Double original) {
        String formattedAmount = String.format("%.2f", original) + "";
        return formattedAmount;
    }

    public static Boolean isLoggedIn() {
        return false;
    }
}
