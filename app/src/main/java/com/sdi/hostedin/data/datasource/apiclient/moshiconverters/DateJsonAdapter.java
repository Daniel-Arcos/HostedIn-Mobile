package com.sdi.hostedin.data.datasource.apiclient.moshiconverters;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateJsonAdapter {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @ToJson
    String toJson(Date date) {
        return dateFormat.format(date);
    }

    @FromJson
    Date fromJson(String dateString) throws Exception {
        return dateFormat.parse(dateString);
    }
}
