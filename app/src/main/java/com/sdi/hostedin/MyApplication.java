package com.sdi.hostedin;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {
    private ExecutorService executorService;
    private Handler mainThreadHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(numberOfCores);
        this.mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Handler getMainThreadHandler() {
        return mainThreadHandler;
    }
}
