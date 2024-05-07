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
import com.sdi.hostedin.databinding.ActivityMainBinding;
import com.sdi.hostedin.feature.guest.GuestMainActivity;
import com.sdi.hostedin.feature.guest.explore.ExploreFragment;
import com.sdi.hostedin.feature.host.HostMainActivity;
import com.sdi.hostedin.feature.login.LoginFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    RxDataStore<Preferences> dataStoreRX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentMainContainer.getId(), LoginFragment.class, null)
                .commit();



    }


}