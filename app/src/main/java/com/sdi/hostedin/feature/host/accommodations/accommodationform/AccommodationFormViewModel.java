package com.sdi.hostedin.feature.host.accommodations.accommodationform;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.MyApplication;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.callbacks.AccommodationCallback;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Location;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.data.repositories.MultimediasRepository;
import com.sdi.hostedin.domain.CreateAccommodationUseCase;
import com.sdi.hostedin.domain.UpdateAccommodationUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;
import com.sdi.hostedin.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

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

    private final ExecutorService executorService;
    private final Handler mainHandler;

    public AccommodationFormViewModel(@NonNull Application application){
        super(application);

        defaultStart();
        MyApplication myApplication = (MyApplication) application;
        executorService = myApplication.getExecutorService();
        mainHandler = myApplication.getMainThreadHandler();
    }

     public void  defaultStart(){
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

    public void createAccommodation(Accommodation accommodation, Context context) {
        CreateAccommodationUseCase createAccommodationUseCase = new CreateAccommodationUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));

        String token = DataStoreAccess.accessToken(getApplication());
        createAccommodationUseCase.createAccommodation(accommodation, token, new AccommodationCallback() {
            @Override
            public void onSuccess(Accommodation accommodation, String message, String newToken) {
                accommodationMutableLiveData.setValue(accommodation);
                uploadAccommodationMultimedia(context);
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.messg_accommodation_created)));
                if(newToken != null && !newToken.isEmpty()){
                    DataStoreAccess.saveToken(getApplication(), newToken);
                }
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                if(newToken != null && !newToken.isEmpty()){
                    DataStoreAccess.saveToken(getApplication(), newToken);
                }
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }

    public void uploadAccommodationMultimedia(Context context) {
        String accommodationId = accommodationMutableLiveData.getValue().getId();

        byte[][] selectedMultimedia = joinMultimedia();
        if (selectedMultimedia != null) {
            MultimediasRepository multimediasRepository = new MultimediasRepository(executorService);

            multimediasRepository.uploadAccommodationMultimedia(accommodationId, selectedMultimedia, new MultimediasRepository.UploadAccommodationMultimediaCallback() {
                @Override
                public void onSuccess(String message) {
                    ToastUtils.showShortInformationMessage(getApplication(), context.getString(R.string.multimedia_saved));
                }

                @Override
                public void onError(String message) {
                    ToastUtils.showShortInformationMessage(getApplication(), context.getString(R.string.error_uploading_multimedia));
                }
            }, mainHandler);
        }
        else
        {
            Log.e("PRUEBA", "No multimedia");
            ToastUtils.showShortInformationMessage(getApplication(), context.getString(R.string.error_trying_upload_multimedia));
        }
    }


    private byte[][] joinMultimedia() {
        int numberOfVideos = 1;
        byte[][] selectedPhotos = this.selectedPhotos.getValue();
        byte[] selectedVideo = this.selectedVideo.getValue();
        byte[][] selectedMultimedia = null;
        if (selectedPhotos != null) {
            selectedMultimedia = new byte[selectedPhotos.length + numberOfVideos][];
            System.arraycopy(selectedPhotos, 0, selectedMultimedia, 0, selectedPhotos.length);
            if (selectedVideo != null) {
                selectedMultimedia[selectedPhotos.length] = selectedVideo;
            }
        }

        return selectedMultimedia;
    }

    public void updateAccommodation(Accommodation accommodation) {
        UpdateAccommodationUseCase updateAccommodationUseCase = new UpdateAccommodationUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));

        String token = DataStoreAccess.accessToken(getApplication());
        if(fragmentNumberMutableLiveData.getValue() == AccommodationMultimediaFragment.LOCAL_FRAGMENT_NUMBER){
            updateAccommodationMultimedia();
        }else{
            updateAccommodationUseCase.updateAccommodation(accommodation, token, new AccommodationCallback() {
                @Override
                public void onSuccess(Accommodation accommodation, String message, String newToken) {
                    accommodationMutableLiveData.setValue(accommodation);
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.messg_accommodation_updated)));
                    if(newToken != null && !newToken.equals("")){
                        DataStoreAccess.saveToken(getApplication(), newToken);
                    }
                }

                @Override
                public void onError(String errorMessage, String newToken) {
                    if(newToken != null && !newToken.isEmpty()){
                        DataStoreAccess.saveToken(getApplication(), newToken);
                    }
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
                }
            });
        }

    }


    public void updateAccommodationMultimedia() {
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_actualizando_imagenes)));
        String accommodationId = accommodationMutableLiveData.getValue().getId();

        byte[][] selectedMultimedia = joinMultimedia();
        if (selectedMultimedia != null) {
            MultimediasRepository multimediasRepository = new MultimediasRepository(executorService);

            multimediasRepository.updateAccommodationMultimedia(accommodationId, selectedMultimedia, new MultimediasRepository.UploadAccommodationMultimediaCallback() {
                @Override
                public void onSuccess(String message) {
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.messg_updated_multimedia)));
                }

                @Override
                public void onError(String message) {
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, getApplication().getString(R.string.messg_updated_multimedia_error)));
                }
            }, mainHandler);
        }
        else
        {
            ToastUtils.showShortInformationMessage(getApplication(), getApplication().getString(R.string.messg_updated_multimedia_error));
        }
    }


}
