package com.sdi.hostedin.data.datasource.apiclient.moshiconverters;

import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseGetAccommodationsObject;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Location;

import java.util.ArrayList;
import java.util.List;

public class MoshiConverter {

    public static ArrayList<Accommodation> convertAPIAccommodationsResponseToJavaObjects(ResponseGetAccommodationsObject responseBody) {
        ArrayList<Accommodation> accommodations = new ArrayList<>();
        for (Accommodation ac: responseBody.getAccommodations()) {
            Accommodation accommodationRes = new Accommodation();
            accommodationRes.setId(ac.getId());
            accommodationRes.setTitle(ac.getTitle());
            accommodationRes.setDescription(ac.getDescription());
            accommodationRes.setRules(ac.getRules());
            accommodationRes.setAccommodationType(ac.getAccommodationType());
            accommodationRes.setNightPrice(ac.getNightPrice());
            accommodationRes.setGuestsNumber(ac.getGuestsNumber());
            accommodationRes.setRoomsNumber(ac.getRoomsNumber());
            accommodationRes.setBedsNumber(ac.getBedsNumber());
            accommodationRes.setBathroomsNumber(ac.getBathroomsNumber());
            accommodationRes.setAccommodationServices(ac.getAccommodationServices());
            accommodationRes.setUser(ac.getUser());
            Location location = new Location();
            location.set_id(ac.getLocation().get_id());
            location.setLongitude(ac.getLocation().getCoordinates()[0]);
            location.setLatitude(ac.getLocation().getCoordinates()[1]);
            accommodationRes.setLocation(location);
            accommodations.add(accommodationRes);
        }
        return accommodations;
    }
}
