package com.sdi.hostedin.data.datasource.local;

import android.app.Activity;
import android.app.Application;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;

public class DataStoreAccess {

    private static RxDataStore<Preferences> dataStoreRX;

    public static String accessUserId(Application application) {
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(application,"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(new Activity(), dataStoreRX);
        return dataStoreHelper.getStringValue("USER_ID");
    }

    public static String accessToken(Application application) {
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(application,"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(new Activity(), dataStoreRX);
        return dataStoreHelper.getStringValue("TOKEN");
    }

    public static void saveToken(Application application, String token) {
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(application,"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(new Activity(), dataStoreRX);
        dataStoreHelper.putStringValue("TOKEN", token);
    }

    public static void saveUserId(Application application, String userId) {
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(application,"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(new Activity(), dataStoreRX);
        dataStoreHelper.putStringValue("USER_ID", userId);
    }

}
