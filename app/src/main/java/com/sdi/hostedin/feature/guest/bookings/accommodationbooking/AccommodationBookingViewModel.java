package com.sdi.hostedin.feature.guest.bookings.accommodationbooking;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.callbacks.BookingCallback;
import com.sdi.hostedin.data.callbacks.BookingsCallback;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.domain.CreateAccommodationUseCase;
import com.sdi.hostedin.domain.CreateBookingUseCase;
import com.sdi.hostedin.domain.GetBookingsOfAccommodationUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;
import com.sdi.hostedin.utils.DateFormatterUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AccommodationBookingViewModel extends AndroidViewModel {
    public static final String SUCCESS_BOOKING_MESSAGE  = "booking created";
    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Booking> bookingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> realGuestsNumber = new MutableLiveData<>();
    private MutableLiveData<String> beginningDate = new MutableLiveData<>();
    private MutableLiveData<String> endingDate = new MutableLiveData<>();
    private MutableLiveData<Integer> stayDays = new MutableLiveData<>();
    private MutableLiveData<Double> totalCost = new MutableLiveData<>();

    public AccommodationBookingViewModel(@NonNull Application application) {
        super(application);
        realGuestsNumber.setValue(1);
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<Booking> getBookingMutableLiveData() {
        return bookingMutableLiveData;
    }

    public MutableLiveData<Integer> getRealGuestsNumber() {
        return realGuestsNumber;
    }

    public MutableLiveData<String> getBeginningDate() {
        return beginningDate;
    }

    public MutableLiveData<String> getEndingDate() {
        return endingDate;
    }

    public MutableLiveData<Integer> getStayDays() {
        return stayDays;
    }

    public MutableLiveData<Double> getTotalCost() {
        return totalCost;
    }
    private MutableLiveData<List<Booking>> bookingsList = new MutableLiveData<>();

    public MutableLiveData<List<Booking>> getBookingsList() {
        return bookingsList;
    }

    public void incrementRealGuestsNumber(int guestsLimit) {
        if (realGuestsNumber.getValue() < guestsLimit) {
            realGuestsNumber.setValue(realGuestsNumber.getValue() + 1);
        }
    }

    public void decrementRealGuestsNumber() {
        if (realGuestsNumber.getValue() > 1) {
            realGuestsNumber.setValue(realGuestsNumber.getValue() - 1);
        }
    }

    public void updateBeginningDate(long beginDate) {
        String formattedDate = DateFormatterUtils.parseLongToString(beginDate);
        beginningDate.setValue(formattedDate);
    }

    public void updateEndingDate(long beginDate) {
        String formattedDate = DateFormatterUtils.parseLongToString(beginDate);
        endingDate.setValue(formattedDate);
    }

    public void calculateStayDays() {
        String beginning = beginningDate.getValue();
        String ending = endingDate.getValue();

        Date beginDate = DateFormatterUtils.parseStringToDate(beginning);
        Date endingDate = DateFormatterUtils.parseStringToDate(ending);

        int milisecondsByDay = 86400000;
        int days = (int) ((endingDate.getTime()-beginDate.getTime()) / milisecondsByDay);

        stayDays.setValue(days + 1);
    }

    public void calculateCost(double nightPrice) {
        int days = stayDays.getValue();
        double subtotal = nightPrice * days;

        totalCost.setValue(subtotal);
    }

    public void createBooking(Booking booking) {
        CreateBookingUseCase createBookingUseCase = new CreateBookingUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));
        String token = DataStoreAccess.accessToken(getApplication());
        createBookingUseCase.createBooking(booking, token, new BookingCallback() {
            @Override
            public void onSuccess(Booking booking, String token) {
                bookingMutableLiveData.setValue(booking);
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, SUCCESS_BOOKING_MESSAGE));
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("PRUEBA", "onError: " + errorMessage);
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }

    public void getAccommodationBookings(String accommodationId) {
        GetBookingsOfAccommodationUseCase getBookingsOfAccommodation = new GetBookingsOfAccommodationUseCase();

        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));
        String token = DataStoreAccess.accessToken(getApplication());
        getBookingsOfAccommodation.getBookingsOfSpecificAccommodation(accommodationId, token, new BookingsCallback() {
            @Override
            public void onSuccess(List<Booking> bookingList, String message) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, message));
                bookingsList.setValue(bookingList);
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }
}
