package com.sdi.hostedin.feature.host;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.fragment.app.FragmentManager;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.ActivityHostMainBinding;
import com.sdi.hostedin.feature.host.accommodations.all.HostOwnedAccommodationsFragment;
import com.sdi.hostedin.feature.host.bookings.HostBookedAccommodationsFragment;
import com.sdi.hostedin.feature.statistics.StatisticsFragment;

public class HostMainActivity extends AppCompatActivity {

    public static final String USER_KEY = "user_key";
    ActivityHostMainBinding binding;
    RxDataStore<Preferences> dataStoreRX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHostMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle extras = getIntent().getExtras();
        User user = (User) extras.getParcelable(USER_KEY);
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this,"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(this, dataStoreRX);
        dataStoreHelper.putBoolValue("START_HOST", true);

        Bundle bundleFragment = new Bundle();
        bundleFragment.putParcelable(HostBookedAccommodationsFragment.USER_KEY, user);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentHostContainer.getId(), HostBookedAccommodationsFragment.class, bundleFragment)
                .commit();

        binding.bottomNavigationViewHost.setOnItemSelectedListener(item ->{
            int itemId = item.getItemId();
            if (itemId == R.id.bookings_host) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(binding.fragmentHostContainer.getId(), HostBookedAccommodationsFragment.class, bundleFragment)
                        .commit();
            } else if (itemId == R.id.publications) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(binding.fragmentHostContainer.getId(), HostOwnedAccommodationsFragment.class, null)
                        .commit();
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(binding.fragmentHostContainer.getId(), StatisticsFragment.class, null)
                        .commit();
            }
            return  true;
        });
    }


}