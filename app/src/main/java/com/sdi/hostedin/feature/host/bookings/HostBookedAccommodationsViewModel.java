package com.sdi.hostedin.feature.host.bookings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.model.User;

public class HostBookedAccommodationsViewModel extends AndroidViewModel {

    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public HostBookedAccommodationsViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void setUserMutableLiveData(User user) {
        this.userMutableLiveData.setValue(user);
    }
}
