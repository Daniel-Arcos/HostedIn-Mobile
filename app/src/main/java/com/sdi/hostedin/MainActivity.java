package com.sdi.hostedin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.ActivityMainBinding;
import com.sdi.hostedin.feature.guest.GuestMainActivity;
import com.sdi.hostedin.feature.host.HostMainActivity;
import com.sdi.hostedin.feature.login.LoginFragment;
import com.sdi.hostedin.feature.login.SigninViewModel;
import com.sdi.hostedin.utils.ErrorMessagesHandler;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    RxDataStore<Preferences> dataStoreRX;
    SigninViewModel signinViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);

        ErrorMessagesHandler.setContext(getApplicationContext());
        signinViewModel =
                new ViewModelProvider(this, new ViewModelFactory(this.getApplication())).get(SigninViewModel.class);
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        manageLoading();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this,"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(this, dataStoreRX);
        boolean isRememberMeActivated = dataStoreHelper.getBoolValue("REMEMBER");
        if (isRememberMeActivated) {
            String email = dataStoreHelper.getStringValue("EMAIL");
            String password = dataStoreHelper.getStringValue("PASSWORD");
            signinViewModel.signIn(email, password);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(binding.fragmentMainContainer.getId(), LoginFragment.class, null)
                    .commit();
        }
    }

    private void manageLoading() {
        signinViewModel.getRequestStatusMutableLiveData().observe(this, status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbLogin.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbLogin.setVisibility(View.GONE);
                    enterToApp();
                    break;
                case ERROR:
                    binding.pgbLogin.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(binding.fragmentMainContainer.getId(), LoginFragment.class, null)
                            .commit();
            }
        });
    }

    private void enterToApp() {
        User user = signinViewModel.getUserMutableLiveData().getValue();
        if (!user.getRoles().contains("Guest")) {
            goToHostMenu();
        } else if (!user.getRoles().contains("Host")) {
            goToGuestMenu();
        } else {
            choseNextActivity();
        }
    }

    private void choseNextActivity() {
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
        if (signinViewModel.getUserMutableLiveData().getValue() != null) {
            intent.putExtra(GuestMainActivity.USER_KEY, signinViewModel.getUserMutableLiveData().getValue());
            startActivity(intent);
            this.finish();
        } else {
            ToastUtils.showShortInformationMessage(this, getString(R.string.there_is_a_problem));
        }
    }

    private void goToHostMenu() {
        Intent intent = new Intent(this, HostMainActivity.class);
        if (signinViewModel.getUserMutableLiveData().getValue() != null) {
            intent.putExtra(HostMainActivity.USER_KEY, signinViewModel.getUserMutableLiveData().getValue());
            startActivity(intent);
            this.finish();
        } else {
            ToastUtils.showShortInformationMessage(this, getString(R.string.there_is_a_problem));
        }
    }
}