package com.sdi.hostedin.data.model;

public class Booking {

    private String _id;
    private String accommodationId;
    private String beginningDate;
    private String endingDate;
    private int numberOfGuests;
    private double totalCost;
    private String bookingStatus;
    private String guestUserId;
    private String guestName;
    private String hostName;


    public Booking() {
    }

    public Booking(String accommodationId,String id, String beginningDate, String endingDate, int numberOfGuest, double totalCost, String bookingStatus, String guestUserId, String guestName, String hostName) {
        this.accommodationId = accommodationId;
        this._id = id;
        this.beginningDate = beginningDate;
        this.endingDate = endingDate;
        this.numberOfGuests = numberOfGuest;
        this.totalCost = totalCost;
        this.bookingStatus = bookingStatus;
        this.guestUserId = guestUserId;
        this.guestName = guestName;
        this.hostName = hostName;
    }

    public String getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(String accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(String beginningDate) {
        this.beginningDate = beginningDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getGuestUserId() {
        return guestUserId;
    }

    public void setGuestUserId(String guestUserId) {
        this.guestUserId = guestUserId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}

