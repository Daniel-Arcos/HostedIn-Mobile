package com.sdi.hostedin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava2.RxDataStore;

import android.os.Bundle;

import androidx.core.splashscreen.SplashScreen;

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