package com.sdi.hostedin.utils;

import static java.text.DateFormat.getDateInstance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormatterUtils {
    private static final String NORMAL_DATE_PATTERN = "dd/MM/yyyy";
    private static final String MONGODB_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private static final String TIME_ZONE = "UTC";


    public static String parseDateForMongoDB(String dayFirst) {
        String formattedDate = dayFirst;
        Date date = parseStringToDate(dayFirst);

        if (date != null) {
            formattedDate = formatMongoDate(date);
        } else {
            formattedDate = null;
        }

        return formattedDate;
    }

    public static Date parseStringToDate(String date) {
        Date parsedDate = null;
        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(NORMAL_DATE_PATTERN);
            parsedDate = dateFormatter.parse(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return parsedDate;
    }

    private static String formatMongoDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(MONGODB_DATE_PATTERN);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));

        return dateFormatter.format(date);
    }

    public static String parseMongoDateToLocal(String mongoDate) {
        String formattedDate = mongoDate;
        Date date = parseMongoDBStringToDate(mongoDate);

        if (date != null) {
            formattedDate = formatNormalDate(date);
        } else {
            formattedDate = null;
        }

        return formattedDate;
    }

    private static Date parseMongoDBStringToDate(String mongoDBDate) {
        Date parsedDate = null;
        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(MONGODB_DATE_PATTERN);
            parsedDate = dateFormatter.parse(mongoDBDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return parsedDate;
    }

    private static String formatNormalDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(NORMAL_DATE_PATTERN);
        return dateFormatter.format(date);
    }
}
