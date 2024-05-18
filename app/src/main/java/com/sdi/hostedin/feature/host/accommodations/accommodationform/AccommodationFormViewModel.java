package com.sdi.hostedin.feature.host.accommodations.accommodationform;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Location;
import com.sdi.hostedin.data.model.Publication;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.enums.AccommodationServices;
import com.sdi.hostedin.ui.RequestStatus;

import java.util.List;

public class AccommodationFormViewModel extends AndroidViewModel {

    private  MutableLiveData<Integer> fragmentNumberMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Publication> publicationMutableLiveData = new MutableLiveData<>();

    public AccommodationFormViewModel(@NonNull Application application) {
        super(application);

        Publication publication = new Publication();
        Accommodation accommodation = new Accommodation();
        User user = new User();
        Location location = new Location();
        accommodation.setLocation(location);
        publication.setAccommodation(accommodation);
        publication.setUser(user);

        publicationMutableLiveData.setValue(publication);
        fragmentNumberMutableLiveData.setValue(1);
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<Publication> getPublicationMutableLiveData() {
        return publicationMutableLiveData;
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
        //publicationMutableLiveData.getValue().getAccommodation().setAccommodationType(accommodationType);
        Publication publication = publicationMutableLiveData.getValue();
        publication.getAccommodation().setAccommodationType(accommodationType);
        publicationMutableLiveData.postValue(publication);
    }

    public void selectAccommodationLocation(Location location) {
        Publication publication = publicationMutableLiveData.getValue();
        publication.getAccommodation().setLocation(location);
        publicationMutableLiveData.postValue(publication);
    }

    public void selectBasics(int guests, int rooms, int beds, int bathrooms) {
        Publication publication = publicationMutableLiveData.getValue();
        publication.getAccommodation().setGuestsNumber(guests);
        publication.getAccommodation().setRoomsNumber(rooms);
        publication.getAccommodation().setBedsNumber(beds);
        publication.getAccommodation().setBathroomsNumber(bathrooms);
        publicationMutableLiveData.postValue(publication);
    }

    public void selectAccommodationServices(String[] accommodationServices) {
        Publication publication = publicationMutableLiveData.getValue();
        publication.getAccommodation().setAccommodationServices(accommodationServices);
        publicationMutableLiveData.postValue(publication);
    }

    public void selectAccommodationInformation(String title, String description, String rules, double price) {
        Publication publication = publicationMutableLiveData.getValue();
        publication.setTitle(title);
        publication.setDescription(description);
        publication.getAccommodation().setRules(rules);
        publication.getAccommodation().setNightPrice(price);
        publicationMutableLiveData.postValue(publication);
    }

    public void selectPhoto() {
        //TODO
        Publication publication = publicationMutableLiveData.getValue();
        publication.setDescription("photo: cambiar esto");
        publicationMutableLiveData.postValue(publication);
    }

}
