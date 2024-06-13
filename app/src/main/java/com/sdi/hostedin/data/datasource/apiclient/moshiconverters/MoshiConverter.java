package com.sdi.hostedin.data.datasource.apiclient.moshiconverters;

import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseBookedAccommodation;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseBookingsListObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseGetAccommodationsObject;
import com.sdi.hostedin.data.datasource.apiclient.responseobjects.ResponseGuestBookedAccommodations;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.BookedAccommodation;
import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.data.model.GuestBooking;
import com.sdi.hostedin.data.model.Location;

import java.util.ArrayList;

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
            accommodationRes.setRate(ac.getRate());
            Location location = new Location();
            location.set_id(ac.getLocation().get_id());
            location.setLongitude(ac.getLocation().getCoordinates()[0]);
            location.setLatitude(ac.getLocation().getCoordinates()[1]);
            location.setAddress(ac.getLocation().getAddress());
            accommodationRes.setLocation(location);
            accommodations.add(accommodationRes);
        }
        return accommodations;
    }


    public static ArrayList<BookedAccommodation> convertAPIBookedAccommodationsResponseToJavaObjects(ResponseBookedAccommodation responseBody) {
        ArrayList<BookedAccommodation> accommodations = new ArrayList<>();
        for (BookedAccommodation ac: responseBody.getAccommodations()) {
            BookedAccommodation accommodationRes = new BookedAccommodation();
            accommodationRes.set_id(ac.get_id());
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
            if(ac.getUser() != null){
                accommodationRes.setUser(ac.getUser());
            }
            Location location = new Location();
            location.set_id(ac.getLocation().get_id());
            location.setLongitude(ac.getLocation().getCoordinates()[0]);
            location.setLatitude(ac.getLocation().getCoordinates()[1]);
            location.setAddress(ac.getLocation().getAddress());
            accommodationRes.setLocation(location);
            accommodations.add(accommodationRes);
        }
        return accommodations;
    }

    public static ArrayList<Booking> convertAPIBookingsResponseToJavaObjects(ResponseBookingsListObject responseBody) {
        ArrayList<Booking> bookings = new ArrayList<>();
        for (Booking ac: responseBody.getBookings()) {
            Booking booking = new Booking();
            Accommodation accommodationRes = new Accommodation();
            accommodationRes.setId(ac.get_id());
            accommodationRes.setTitle(ac.getAccommodation().getTitle());
            accommodationRes.setDescription(ac.getAccommodation().getDescription());
            accommodationRes.setRules(ac.getAccommodation().getRules());
            accommodationRes.setAccommodationType(ac.getAccommodation().getAccommodationType());
            accommodationRes.setNightPrice(ac.getAccommodation().getNightPrice());
            accommodationRes.setGuestsNumber(ac.getAccommodation().getGuestsNumber());
            accommodationRes.setRoomsNumber(ac.getAccommodation().getRoomsNumber());
            accommodationRes.setBedsNumber(ac.getAccommodation().getBedsNumber());
            accommodationRes.setBathroomsNumber(ac.getAccommodation().getBathroomsNumber());
            accommodationRes.setAccommodationServices(ac.getAccommodation().getAccommodationServices());
            if(ac.getAccommodation().getUser() != null){
                accommodationRes.setUser(ac.getAccommodation().getUser());
            }
            Location location = new Location();
            location.set_id(ac.getAccommodation().getLocation().get_id());
            location.setLongitude(ac.getAccommodation().getLocation().getCoordinates()[0]);
            location.setLatitude(ac.getAccommodation().getLocation().getCoordinates()[1]);
            location.setAddress(ac.getAccommodation().getLocation().getAddress());
            accommodationRes.setLocation(location);

            booking.set_id(ac.get_id());
            booking.setBookingStatus(ac.getBookingStatus());
            booking.setTotalCost(ac.getTotalCost());
            booking.setEndingDate(ac.getEndingDate());
            booking.setBeginningDate(ac.getBeginningDate());
            booking.setNumberOfGuests(ac.getNumberOfGuests());
            if(ac.getGuestUser()!= null){
                booking.setGuestUser(ac.getGuestUser());
            }
            if(ac.getHostUser() != null){
                booking.setHostUser(ac.getHostUser());
            }
            booking.setAccommodation(accommodationRes);
            bookings.add(booking);
        }
        return bookings;
    }

    public static ArrayList<GuestBooking> convertAPIGuestBookingsResponseToJavaObjects(ResponseGuestBookedAccommodations responseBody) {
        ArrayList<GuestBooking> bookings = new ArrayList<>();
        for (GuestBooking ac: responseBody.getAccommodationsBooked()) {
            GuestBooking booking = new GuestBooking();
            Accommodation accommodationRes = new Accommodation();
            accommodationRes.setId(ac.getAccommodation().getId());
            accommodationRes.setTitle(ac.getAccommodation().getTitle());
            accommodationRes.setDescription(ac.getAccommodation().getDescription());
            accommodationRes.setRules(ac.getAccommodation().getRules());
            accommodationRes.setAccommodationType(ac.getAccommodation().getAccommodationType());
            accommodationRes.setNightPrice(ac.getAccommodation().getNightPrice());
            accommodationRes.setGuestsNumber(ac.getAccommodation().getGuestsNumber());
            accommodationRes.setRoomsNumber(ac.getAccommodation().getRoomsNumber());
            accommodationRes.setBedsNumber(ac.getAccommodation().getBedsNumber());
            accommodationRes.setBathroomsNumber(ac.getAccommodation().getBathroomsNumber());
            accommodationRes.setAccommodationServices(ac.getAccommodation().getAccommodationServices());
            if(ac.getAccommodation().getUser() != null){
                accommodationRes.setUser(ac.getAccommodation().getUser());
            }
            Location location = new Location();
            location.set_id(ac.getAccommodation().getLocation().get_id());
            location.setLongitude(ac.getAccommodation().getLocation().getCoordinates()[0]);
            location.setLatitude(ac.getAccommodation().getLocation().getCoordinates()[1]);
            location.setAddress(ac.getAccommodation().getLocation().getAddress());
            accommodationRes.setLocation(location);

            booking.set_id(ac.get_id());
            booking.setBookingStatus(ac.getBookingStatus());
            booking.setTotalCost(ac.getTotalCost());
            booking.setEndingDate(ac.getEndingDate());
            booking.setBeginningDate(ac.getBeginningDate());
            booking.setNumberOfGuests(ac.getNumberOfGuests());
            if(ac.getGuestUser()!= null){
                booking.setGuestUser(ac.getGuestUser());
            }
            if(ac.getHostUser() != null){
                booking.setHostUser(ac.getHostUser());
            }
            booking.setAccommodation(accommodationRes);
            bookings.add(booking);
        }
        return bookings;
    }
}
