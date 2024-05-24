package com.sdi.hostedin.feature.host.accommodations.accommodationform;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.callbacks.AccommodationCallback;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Location;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.domain.CreateAccommodationUseCase;
import com.sdi.hostedin.domain.UpdateAccommodationUseCase;
import com.sdi.hostedin.grpc.GrpcAccommodationMultimedia;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

import java.util.ArrayList;
import java.util.List;

public class AccommodationFormViewModel extends AndroidViewModel {

    private  MutableLiveData<Integer> fragmentNumberMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Accommodation> accommodationMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<byte[][]> selectedPhotos = new MutableLiveData<>();
    private MutableLiveData<byte[]> selectedVideo = new MutableLiveData<>();
    private MutableLiveData<Integer> selectedAccommodationType = new MutableLiveData<>();
    private MutableLiveData<List<Integer>> servicesNumber = new MutableLiveData<>();
    private MutableLiveData<List<Uri>> imagesUri = new MutableLiveData<>();
    private MutableLiveData<Uri> videoUri = new MutableLiveData<>();
    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<String> description = new MutableLiveData<>();
    private MutableLiveData<String> rules = new MutableLiveData<>();
    private MutableLiveData<Double> price = new MutableLiveData<>();
    RxDataStore<Preferences> dataStoreRX;

    public AccommodationFormViewModel(@NonNull Application application){
        super(application);

        defaultStart();
    }

    public void restartViewModel(){
        defaultStart();
    }

    private void  defaultStart(){
        Accommodation accommodation = new Accommodation();
        Location location = new Location();
        accommodation.setLocation(location);
        accommodation.setMultimediaSelected(false);

        accommodationMutableLiveData.setValue(accommodation);
        fragmentNumberMutableLiveData.setValue(1);
        assignUserId();
        servicesNumber.setValue(new ArrayList<>());
        imagesUri.setValue(new ArrayList<>());
    }

    public void setAccommodationMutableLiveData(Accommodation accommodation) {
        this.accommodationMutableLiveData.setValue(accommodation);
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
        User user = new User();
        user.setId(userId);
        accommodationMutableLiveData.getValue().setUser(user);
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

    public MutableLiveData<Integer> getSelectedAccommodationType() {
        return selectedAccommodationType;
    }

    public MutableLiveData<List<Integer>> getServicesNumber() {
        return servicesNumber;
    }

    public MutableLiveData<List<Uri>> getImagesUri() {
        return imagesUri;
    }

    public MutableLiveData<Uri> getVideoUri() {
        return videoUri;
    }

    public void addServicerNumber(int num) {
        if (!servicesNumber.getValue().contains(num)){
            servicesNumber.getValue().add(num);
        }
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public MutableLiveData<String> getDescription() {
        return description;
    }

    public MutableLiveData<String> getRules() {
        return rules;
    }

    public MutableLiveData<Double> getPrice() {
        return price;
    }

    public MutableLiveData<byte[][]> getSelectedPhotos() {
        return selectedPhotos;
    }

    public MutableLiveData<byte[]> getSelectedVideo() {
        return selectedVideo;
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

    public void selectMultimedia(byte[][] selectedPhotos, byte[]selectedVideo) {
        this.selectedPhotos.setValue(selectedPhotos);
        this.selectedVideo.setValue(selectedVideo);

        Accommodation accommodation = accommodationMutableLiveData.getValue();
        accommodation.setMultimediaSelected(true);
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

        String token = DataStoreAccess.accessToken(getApplication());
        createAccommodationUseCase.createAccommodation(accommodation, token, new AccommodationCallback() {
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

    public void uploadAccommodationMultimedia() {
        String accommodationId = accommodationMutableLiveData.getValue().getId();
        byte[][] selectedPhotos = this.selectedPhotos.getValue();
        byte[] selectedVideo = this.selectedVideo.getValue();

        GrpcAccommodationMultimedia grpcClient = new GrpcAccommodationMultimedia();

        for (byte[] photo : selectedPhotos) {
            grpcClient.uploadAccommodationMultimedia(accommodationId, photo);
        }

        grpcClient.uploadAccommodationMultimedia(accommodationId, selectedVideo);
    }

    public void updateAccommodation(Accommodation accommodation) {
        UpdateAccommodationUseCase updateAccommodationUseCase = new UpdateAccommodationUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));

        String token = DataStoreAccess.accessToken(getApplication());
        updateAccommodationUseCase.updateAccommodation(accommodation, token, new AccommodationCallback() {
            @Override
            public void onSuccess(Accommodation accommodation, String token) {
                accommodationMutableLiveData.setValue(accommodation);
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, "Accommodation updated"));
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }

}
