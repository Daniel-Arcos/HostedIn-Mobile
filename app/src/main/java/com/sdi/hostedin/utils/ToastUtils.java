package com.sdi.hostedin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.sdi.hostedin.R;

public class ToastUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static void setContext(Context context) {
        ToastUtils.context = context;
    }

    public static void showShortInformationMessage(Context context, String informationText) {
        Toast toast = Toast.makeText(context, informationText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0,0);
        toast.show();
    }
    public static void showLongInformationMessage(Context context, String informationText) {
        Toast toast = Toast.makeText(context, informationText, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0,0);
        toast.show();
    }

    public static String getGenericErrorMessageConection(){
        return context.getString(R.string.messg_generic_error_connection);
    }
}
