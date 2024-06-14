package com.sdi.hostedin.utils;

import android.content.Context;
import android.view.ContentInfo;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

public class ProgressBarUtils {
    public static void observeRequestStatus(MutableLiveData<RequestStatus> liveData,
                                                LifecycleOwner owner,
                                                ProgressBar pgbCircle,
                                                View vwLoading,
                                                Context context) {
        liveData.observe(owner, status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    pgbCircle.setVisibility(View.VISIBLE);
                    vwLoading.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    pgbCircle.setVisibility(View.GONE);
                    vwLoading.setVisibility(View.GONE);
                    break;
                case ERROR:
                    pgbCircle.setVisibility(View.GONE);
                    vwLoading.setVisibility(View.GONE);
                    ToastUtils.showShortInformationMessage(context, status.getMessage());
            }
        });
    }
}
