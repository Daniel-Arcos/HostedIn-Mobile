package com.sdi.hostedin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatterUtils {
    private static final String NORMAL_DATE_PATTERN = "dd/MM/yyyy";
    private static final String MONGODB_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private static final String NATURAL_DATE = "EEEE d 'de' MMMM 'del' yyyy";
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
            dateFormatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
            parsedDate = dateFormatter.parse(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return parsedDate;
    }

    public static String parseMongoDateToNaturalDate(String dateString){
        String formattedDate = "";
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(MONGODB_DATE_PATTERN, Locale.getDefault());
        Date dateFormatted = null;
        try{
            dateFormatted = inputDateFormat.parse(dateString);
            if (dateFormatted != null){
                SimpleDateFormat outPutFormat = new SimpleDateFormat(NATURAL_DATE, new Locale("es", "Es"));
                formattedDate = outPutFormat.format(dateFormatted);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return formattedDate;
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

    public static String formatNormalDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(NORMAL_DATE_PATTERN);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormatter.format(date);
    }

    public static String parseLongToString(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date newDate = parseLongToDate(date);
        String startDateFormatted = sdf.format(newDate);

        return startDateFormatted;
    }

    public static Date parseLongToDate(long date) {
        Date newDate = new Date(date);
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        newDate.setTime(newDate.getTime() + utcTimeZone.getRawOffset());

        return newDate;
    }

    public static long parseDateToMillis(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(year, month - 1, day, 0, 0, 0); // Mes en base 0
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
}
