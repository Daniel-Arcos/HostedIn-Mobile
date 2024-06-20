package com.sdi.hostedin.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

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


}
