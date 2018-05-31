package com.insyslab.tooz.utils;

import android.util.Patterns;

public class Validator {

//    public static boolean isValidEmail(String email) {
//        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
//        Matcher matcher = pattern.matcher(email);
//        return matcher.matches();
//    }

//    public static boolean isValidPassword(String password) {
//        return password.length() >= 5;
//    }

    public static boolean isValidMobileNumber(String mobile_no) {
        return Patterns.PHONE.matcher(mobile_no).matches() && mobile_no.trim().length() == 10;
    }

    public static boolean isValidName(String name) {
        return name.length() > 3;
    }

//    public static boolean hasText(EditText editText, String errMsg) {
//        if (editText.getEditableText().toString().length() > 0) {
//            return true;
//        }
//        editText.requestFocus();
//        editText.setError(errMsg);
//        return false;
//    }

//    public static boolean validate(final String password) {
//        String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
//        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
//        Matcher matcher = pattern.matcher(password);
//        return matcher.matches();
//    }

//    public static boolean isValidPincode(String pincode) {
//        return pincode.length() == 6;
//    }
}