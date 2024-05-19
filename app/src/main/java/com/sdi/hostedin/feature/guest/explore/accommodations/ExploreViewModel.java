package com.sdi.hostedin.feature.guest.explore.accommodations;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.ui.RequestStatus;

public class ExploreViewModel extends AndroidViewModel {

    MutableLiveData<String> placeToSearch = new MutableLiveData<>();
    MutableLiveData<Accommodation> accommodations = new MutableLiveData<>();
    public ExploreViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getPlaceToSearch() {
        return placeToSearch;
    }

    public void setPlaceToSearch(String placeToSearch) {
        this.placeToSearch.setValue(placeToSearch);
    }
}
