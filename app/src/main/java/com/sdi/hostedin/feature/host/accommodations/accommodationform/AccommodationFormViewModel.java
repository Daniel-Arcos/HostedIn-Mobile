package com.sdi.hostedin.feature.host.accommodations.accommodationform;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.callbacks.AccommodationCallback;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Location;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.domain.CreateAccommodationUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

public class AccommodationFormViewModel extends AndroidViewModel {

    private  MutableLiveData<Integer> fragmentNumberMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Accommodation> accommodationMutableLiveData = new MutableLiveData<>();
    RxDataStore<Preferences> dataStoreRX;

    public AccommodationFormViewModel(@NonNull Application application) {
        super(application);

        Accommodation accommodation = new Accommodation();
        Location location = new Location();
        accommodation.setLocation(location);

        accommodationMutableLiveData.setValue(accommodation);
        fragmentNumberMutableLiveData.setValue(1);
        assignUserId();
    }

    private void assignUserId() {
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this.getApplication(),"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(new AccommodationFormActivity(), dataStoreRX);
        String userId = dataStoreHelper.getStringValue("USER_ID");

        accommodationMutableLiveData.getValue().setUserId(userId);
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<Accommodation> getAccommodationMutableLiveData() {
        return accommodationMutableLiveData;
    }

    public MutableLiveData<Integer> getFragmentNumberMutableLiveData() {
        return fragmentNumberMutableLiveData;
    }

    public void nextFragment(int nextFragment) {
        fragmentNumberMutableLiveData.setValue(nextFragment);
    }

    public void backFragment(int currentFragment) {
        if (currentFragment > 1) {
            fragmentNumberMutableLiveData.setValue(currentFragment - 1);
        } else {
            fragmentNumberMutableLiveData.setValue(1);
        }
    }

    public void selectAccommodationType(String accommodationType) {
        Accommodation accommodation = accommodationMutableLiveData.getValue();
        accommodation.setAccommodationType(accommodationType);
        accommodationMutableLiveData.postValue(accommodation);
    }

    public void selectAccommodationLocation(Location location) {
        Accommodation accommodation = accommodationMutableLiveData.getValue();
        accommodation.setLocation(location);
        accommodationMutableLiveData.postValue(accommodation);
    }

    public void selectBasics(int guests, int rooms, int beds, int bathrooms) {
        Accommodation accommodation = accommodationMutableLiveData.getValue();
        accommodation.setGuestsNumber(guests);
        accommodation.setRoomsNumber(rooms);
        accommodation.setBedsNumber(beds);
        accommodation.setBathroomsNumber(bathrooms);
        accommodationMutableLiveData.postValue(accommodation);
    }

    public void selectAccommodationServices(String[] accommodationServices) {
        Accommodation accommodation = accommodationMutableLiveData.getValue();
        accommodation.setAccommodationServices(accommodationServices);
        accommodationMutableLiveData.postValue(accommodation);
    }

    public void selectAccommodationInformation(String title, String description, String rules, double price) {
        Accommodation accommodation = accommodationMutableLiveData.getValue();
        accommodation.setTitle(title);
        accommodation.setDescription(description);
        accommodation.setRules(rules);
        accommodation.setNightPrice(price);
        accommodationMutableLiveData.postValue(accommodation);
    }

    public void selectPhoto() {
        //TODO
        Accommodation accommodation = accommodationMutableLiveData.getValue();
        accommodation.setDescription("photo: cambiar esto");
        accommodationMutableLiveData.postValue(accommodation);
    }

    public void createAccommodation(Accommodation accommodation) {
        CreateAccommodationUseCase createAccommodationUseCase = new CreateAccommodationUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));

        createAccommodationUseCase.createAccommodation(accommodation, new AccommodationCallback() {
            @Override
            public void onSuccess(Accommodation accommodation, String token) {
                accommodationMutableLiveData.setValue(accommodation);
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, "Accommodation created"));
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }

}
