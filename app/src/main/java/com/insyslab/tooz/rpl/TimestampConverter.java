package com.insyslab.tooz.rpl;

import android.arch.persistence.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.insyslab.tooz.utils.Util.DEFAULT_DATE_FORMAT;

/**
 * Created by TaNMay on 18/01/18.
 */

public class TimestampConverter {

    @TypeConverter
    public static Date fromTimestamp(String value) {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

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
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        if (date != null) return df.format(date);
        else return null;
    }

}
