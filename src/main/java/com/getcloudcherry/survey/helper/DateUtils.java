package com.getcloudcherry.survey.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String getCurrentTimeStamp(long iTimeMillis) {
        Date d = new Date(iTimeMillis);
        SimpleDateFormat aTimeStampFormatter = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        String aTimeStamp = aTimeStampFormatter.format(d);
        return aTimeStamp;
    }

}
