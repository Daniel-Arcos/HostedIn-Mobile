package com.sdi.hostedin.feature.guest;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.search.SearchView;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.ActivityGuestMainActiviyBinding;
import com.sdi.hostedin.feature.guest.bookings.booked_accommodations_list.GuestBookingsFragment;
import com.sdi.hostedin.feature.guest.explore.accommodations.ExploreFragment;
import com.sdi.hostedin.feature.host.bookings.HostBookedAccommodationsFragment;
import com.sdi.hostedin.feature.statistics.StatisticsFragment;

public class GuestMainActivity extends AppCompatActivity {
    public static final String USER_KEY = "user_key";
    ActivityGuestMainActiviyBinding binding;
    RxDataStore<Preferences> dataStoreRX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuestMainActiviyBinding.inflate(getLayoutInflater());
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
        dataStoreHelper.putBoolValue("START_HOST", false);

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleOnPressedButton();
            }
        });

        Bundle bundleFragment = new Bundle();
        bundleFragment.putParcelable(ExploreFragment.USER_KEY, user);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), ExploreFragment.class, bundleFragment)
                .commit();

        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
            int itemId = item.getItemId();
            if (itemId == R.id.explore) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(binding.fragmentContainer.getId(), ExploreFragment.class, bundleFragment)
                        .commit();
            } else if (itemId == R.id.bookings) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(binding.fragmentContainer.getId(), GuestBookingsFragment.class, null)
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

    private void handleOnPressedButton() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof ExploreFragment) {
            SearchView searchView = findViewById(R.id.search_view);
            if (searchView.isShowing()) {
                searchView.hide();
            } else {
                finish();
            }
        } else {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(binding.fragmentContainer.getId(), ExploreFragment.class, null)
                    .commit();
            binding.bottomNavigationView.setSelectedItemId(R.id.explore);
        }
    }
}