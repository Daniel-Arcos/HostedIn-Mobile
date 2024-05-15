package com.sdi.hostedin.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static void showShortInformationMessage(Context context, String informationText) {
        Toast.makeText(context, informationText, Toast.LENGTH_SHORT).show();
    }
}
