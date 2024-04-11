package com.sdi.hostedin.feature.guest;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.databinding.ActivityGuestMainActiviyBinding;
import com.sdi.hostedin.feature.guest.bookings.BookingsFragment;
import com.sdi.hostedin.feature.guest.explore.ExploreFragment;
import com.sdi.hostedin.feature.guest.statistics.StatisticsFragment;

public class GuestMainActivity extends AppCompatActivity {
    ActivityGuestMainActiviyBinding binding;
    RxDataStore<Preferences> dataStoreRX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuestMainActiviyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this,"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(this, dataStoreRX);
        dataStoreHelper.putBoolValue("START_HOST", false);


        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (fragment instanceof ExploreFragment) {
                    finish();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(binding.fragmentContainer.getId(), ExploreFragment.class, null)
                            .commit();
                    binding.bottomNavigationView.setSelectedItemId(R.id.explore);
                }
            }
        });

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), ExploreFragment.class, null)
                .commit();

        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
            int itemId = item.getItemId();
            if (itemId == R.id.explore) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(binding.fragmentContainer.getId(), ExploreFragment.class, null)
                        .commit();
            } else if (itemId == R.id.bookings) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(binding.fragmentContainer.getId(), BookingsFragment.class, null)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(binding.fragmentContainer.getId(), StatisticsFragment.class, null)
                        .commit();
            }
            return  true;
        });
    }
}