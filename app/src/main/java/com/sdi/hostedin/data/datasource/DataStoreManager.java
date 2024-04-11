package com.sdi.hostedin.data.datasource;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava2.RxDataStore;

public class DataStoreManager {

    RxDataStore<Preferences> datastore;
    private static final DataStoreManager ourInstance = new DataStoreManager();
    public static DataStoreManager getInstance() {
        return ourInstance;
    }
    private DataStoreManager() { }
    public void setDataStore(RxDataStore<Preferences> datastore) {
        this.datastore = datastore;
    }
    public RxDataStore<Preferences> getDataStore() {
        return datastore;
    }
}
