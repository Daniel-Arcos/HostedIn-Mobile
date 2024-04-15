package com.sdi.hostedin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.splashscreen.SplashScreen;

import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.feature.guest.GuestMainActivity;
import com.sdi.hostedin.feature.host.HostMainActivity;

public class MainActivity extends AppCompatActivity {

    RxDataStore<Preferences> dataStoreRX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this,"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(this, dataStoreRX);
        boolean isHostEstablished = dataStoreHelper.getBoolValue("START_HOST");
        if (isHostEstablished) {
            goToHostMenu();
        } else {
            goToGuestMenu();
        }
    }

    private void goToGuestMenu() {
        Intent intent = new Intent(this, GuestMainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void goToHostMenu() {
        Intent intent = new Intent(this, HostMainActivity.class);
        startActivity(intent);
        this.finish();
    }
}