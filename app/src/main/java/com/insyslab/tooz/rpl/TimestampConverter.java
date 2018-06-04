package com.insyslab.tooz.rpl;

import android.arch.persistence.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.insyslab.tooz.utils.Util.DEFAULT_DATE_FORMAT;

public class TimestampConverter {

    @TypeConverter
    public static Date fromTimestamp(String value) {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());

        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else return null;

    }

    @TypeConverter
    public static String toTimestamp(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        if (date != null) return df.format(date);
        else return null;
    }

}
