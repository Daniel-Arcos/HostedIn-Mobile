package com.sdi.hostedin.feature.host;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import android.content.Intent;
import android.os.Bundle;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.databinding.ActivityGuestMainActiviyBinding;
import com.sdi.hostedin.databinding.ActivityHostMainBinding;
import com.sdi.hostedin.feature.guest.GuestMainActivity;

public class HostMainActivity extends AppCompatActivity {

    ActivityHostMainBinding binding;
    RxDataStore<Preferences> dataStoreRX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHostMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this,"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(this, dataStoreRX);
        dataStoreHelper.putBoolValue("START_HOST", true);
        binding.changeToGuestBtn.setOnClickListener(v -> changeToGuestMenu());
    }

    private void changeToGuestMenu() {
        Intent intent = new Intent(this, GuestMainActivity.class);
        startActivity(intent);
        this.finish();
    }
}