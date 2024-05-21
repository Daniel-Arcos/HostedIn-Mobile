package com.sdi.hostedin.feature.guest.explore.accommodations;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.callbacks.AccommodationsCallback;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.domain.GetAccommodationsUseCase;
import com.sdi.hostedin.feature.guest.GuestMainActivity;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

import java.util.List;

public class ExploreViewModel extends AndroidViewModel {


    MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> placeToSearch = new MutableLiveData<>();
    MutableLiveData<List<Accommodation>> accommodationsLiveData = new MutableLiveData<>();
    MutableLiveData<Boolean> isNew = new MutableLiveData<>();
    MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();


    public ExploreViewModel(@NonNull Application application) {
        super(application);
        this.isNew.setValue(true);
    }

    public MutableLiveData<String> getPlaceToSearch() {
        return placeToSearch;
    }

    public void setPlaceToSearch(String placeToSearch) {
        this.placeToSearch.setValue(placeToSearch);
    }

    public MutableLiveData<List<Accommodation>> getAccommodationsLiveData() {
        return accommodationsLiveData;
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew.setValue(isNew);
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void setUserMutableLiveData(User user) {
        this.userMutableLiveData.setValue(user);
    }

    public void getAllAccommodationsExceptUserAccommodations() {
        GetAccommodationsUseCase getAccommodationsUseCase = new GetAccommodationsUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));
        String idUser = DataStoreAccess.accessUserId(this.getApplication());
        if (idUser != null) {
            String token = DataStoreAccess.accessToken(getApplication());
            getAccommodationsUseCase.getAllAccommodationsExceptUserAccommodations(idUser, token, new AccommodationsCallback() {
                @Override
                public void onSuccess(List<Accommodation> accommodations, String token) {
                    accommodationsLiveData.setValue(accommodations);
                    if (token != null && !token.equals("")) {
                        DataStoreAccess.saveToken(getApplication(), token);
                    }
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, "Accommodations"));
                }

                @Override
                public void onError(String errorMessage) {
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
                }
            });
        }
    }

    public void searchAccommodations(double lat, double lng) {
        GetAccommodationsUseCase getAccommodationsUseCase = new GetAccommodationsUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));
        String idUser = DataStoreAccess.accessUserId(this.getApplication());
        if (idUser != null) {
            String token = DataStoreAccess.accessToken(getApplication());
            getAccommodationsUseCase.getAllAccommodationsByLocationExceptUserAccommodations(idUser, lat, lng, token, new AccommodationsCallback() {
                @Override
                public void onSuccess(List<Accommodation> accommodations, String token) {
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, "Accommodations"));
                    accommodationsLiveData.setValue(accommodations);
                    if (token != null && !token.equals("")) {
                        DataStoreAccess.saveToken(getApplication(), token);
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
                }
            });
        }
    }
}
