package com.insyslab.tooz.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;
import com.insyslab.tooz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

    public static String getDateInDefaultDateFormat(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public static Calendar getCalenderFormatDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
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
        return new DateFormatSymbols().getWeekdays()[intDay].substring(0, 3).toUpperCase();
    }

    public static String getAmPmFromIndex(Integer intMeridian) {
        return intMeridian == 0 ? "AM" : "PM";
    }

    public static String getFormattedHourOrMinute(Integer intValue) {
        if (intValue < 10) return "0" + intValue;
        else return intValue + "";
    }

    public static String getMonthFromIndex(Integer intMonth) {
        return new DateFormatSymbols().getMonths()[intMonth].substring(0, 3);
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

    public static Boolean isLoggedIn(Context context) {
        return LocalStorage.getInstance(context).getToken() != null && !LocalStorage.getInstance(context).isFirstLogin();
    }

    public static String getFormattedMobileNumber(String mobile) {
        String formattedNumber = "+91 ";
        formattedNumber += mobile.substring(0, 5);
        formattedNumber += " ";
        formattedNumber += mobile.substring(5);
        return formattedNumber;
    }

    public static String getCompactMobileNumber(String contactNumber) {
//        contactNumber = contactNumber.replaceAll("\\D", "");
        if (contactNumber.length() >= 10) {
            if (contactNumber.startsWith("+91")) {
                return contactNumber.substring(3).replaceAll("\\D", "");
            } else {
                return contactNumber.replaceAll("\\D", "");
            }
        } else {
            return null;
        }
    }

    public static String getReminderRemainingTime(Calendar calReminderTime, Calendar currentTime) {
        Long reminderTimeMillis = calReminderTime.getTimeInMillis();
        Long currentTimeMillis = currentTime.getTimeInMillis();
        Long timeDifference = reminderTimeMillis - currentTimeMillis;

        Long timeDiffInSec = timeDifference / 1000;
        if (timeDiffInSec >= 60) {
            Long timeDiffInMin = timeDiffInSec / 60;
            Long remSec = timeDiffInSec % 60;

            if (timeDiffInMin >= 60) {
                Long timeDiffInHour = timeDiffInMin / 60;
                Long remMin = timeDiffInMin % 60;

                if (timeDiffInHour >= 24) {
                    Long timeDiffInDays = timeDiffInHour / 24;
                    Long remHour = timeDiffInHour % 24;

                    return timeDiffInDays + " day" + (timeDiffInDays > 1 ? "s " : " ");
                } else {
                    return timeDiffInHour + " hour" + (timeDiffInHour > 1 ? "s " : " ") + (remMin > 0 ? remMin + " min" : "");
                }
            } else {
                return timeDiffInMin + " min " + (remSec > 0 ? remSec + " sec" : "");
            }
        } else {
            return timeDiffInSec + " second" + (timeDiffInSec > 1 ? "s " : " ");
        }
    }

    public static String getReminderFormatedDate(Calendar cal) {
        String date = cal.get(Calendar.DAY_OF_MONTH) + getDateExtension(cal.get(Calendar.DAY_OF_MONTH));
        String month = getMonthFromIndex(cal.get(Calendar.MONTH));
        String day = getDayOfWeekFromIndex(cal.get(Calendar.DAY_OF_WEEK));

        return date + " " + month + " (" + day + ")," + " " + cal.get(Calendar.YEAR);
    }

    public static String getReminderFormatedTime(Calendar cal) {
        String meridian = getAmPmFromIndex(cal.get(Calendar.AM_PM));
        String hour = getFormattedHourOrMinute(cal.get(Calendar.HOUR));
        String minute = getFormattedHourOrMinute(cal.get(Calendar.MINUTE));

        return hour + ":" + minute + " " + meridian;
    }

    public static String getAddressFromCoords(Context context, LatLng location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String finalAddress = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String postalCode = addresses.get(0).getPostalCode();
            String country = addresses.get(0).getCountryName();

            if (address != null) finalAddress += address;
            if (city != null) finalAddress += ", " + city;
            if (state != null) finalAddress += ", " + state;
            if (postalCode != null) finalAddress += " - " + postalCode;
            if (country != null) finalAddress += ", " + country.toUpperCase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalAddress;
    }

}
