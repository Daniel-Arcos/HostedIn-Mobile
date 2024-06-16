package com.sdi.hostedin.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.sdi.hostedin.R;

public class ErrorMessagesHandler {
    @SuppressLint("StaticFieldLeak")
    private static Context context;


    public static void setContext(Context context) {
        ErrorMessagesHandler.context = context;
    }

    public static String getGenericErrorMessageConnection(){
        return context.getString(R.string.messg_generic_error_connection);
    }

    public static String getErrorLoadingAccommodations(){
        return context.getString(R.string.messg_error_loading_acc);
    }

    public static String getErrorCreatingAccommodation(){
        return context.getString(R.string.messg_error_create_acco);
    }

    public static String getErrorLoadingBookings(){
        return context.getString(R.string.messg_error_get_bookings);
    }

    public static String getErrorMessageCreatingBooking(){
        return context.getString(R.string.messg_error_creating_booking);
    }

    public static String getErrorMessageCreatingCancellation(){
        return context.getString(R.string.messg_error_creating_cancelation);
    }

    public static String getErrorMessageSendingCode(){
        return context.getString(R.string.messg_error_send_code);
    }

    public static String getErrorVerifyingCode(){
        return context.getString(R.string.messg_error_verify_code);
    }

    public static String getErrorUpdatingPassword(){
        return context.getString(R.string.messg_error_update_passw);
    }

    public static String getErrorMessageCreateReview(){
        return context.getString(R.string.messg_error_create_review);
    }

    public static  String getErrorMessageLoadingReviews(){
        return context.getString(R.string.messg_error_loading_review);
    }

    public static String getErrorUpdatingUserProfile(){
        return context.getString(R.string.messg_error_update_profile);
    }

    public static String getErrorDeletingUserProfile(){
        return context.getString(R.string.messg_error_delete_profile);
    }
}
