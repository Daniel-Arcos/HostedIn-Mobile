package com.sdi.hostedin.utils;

import android.content.Context;

import com.sdi.hostedin.R;
import com.sdi.hostedin.enums.BookingSatuses;

public  class TranslatorToSpanish {

    public static String getBookingSpanishStatus(Context context,String status){
        String spanishStatus = "";
        if(BookingSatuses.CURRENT.getDescription().equals(status)){
            spanishStatus = context.getString(R.string.hint_current);
        } else if (BookingSatuses.OVERDUE.getDescription().equals(status)) {
            spanishStatus = context.getString(R.string.hint_overdue);
        }
        else {
            spanishStatus = context.getString(R.string.hint_cancelled);
        }
        return spanishStatus;
    };

}
