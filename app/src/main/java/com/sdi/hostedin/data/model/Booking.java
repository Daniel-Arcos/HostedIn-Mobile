package com.sdi.hostedin.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Booking implements Parcelable {

    private String _id;
    private String accommodationId;
    private String beginningDate;
    private String endingDate;
    private int numberOfGuests;
    private int totalCost;
    private String bookingStatus;
    private User guestUser;
    private User hostUser;
    private String guestUserId;
    private String guestName;
    private String hostName;

    public Booking() {
    }

    public Booking(String _id,String accommodationId, String beginningDate, String endingDate, int numberOfGuest, double totalCost, String bookingStatus,
                   User guestUser, User hostUser, String guestUserId, String guestName, String hostName) {
        this.accommodationId = accommodationId;
        this._id = _id;
        this.beginningDate = beginningDate;
        this.endingDate = endingDate;
        this.numberOfGuests = numberOfGuest;
        this.totalCost = totalCost;
        this.bookingStatus = bookingStatus;
        this.guestUser = guestUser;
        this.hostUser = hostUser;
        this.guestUserId = guestUserId;
        this.guestName = guestName;
        this.hostName = hostName;
    }

    public Booking(String _id,String accommodationId, String beginningDate, String endingDate, int numberOfGuest, double totalCost, String bookingStatus,
                   String guestUserId, String guestName, String hostName) {
        this.accommodationId = accommodationId;
        this._id = _id;
        this.beginningDate = beginningDate;
        this.endingDate = endingDate;
        this.numberOfGuests = numberOfGuest;
        this.totalCost = totalCost;
        this.bookingStatus = bookingStatus;
        this.guestUserId = guestUserId;
        this.guestName = guestName;
        this.hostName = hostName;
    }

    protected Booking(Parcel in) {
        _id = in.readString();
        accommodationId = in.readString();
        beginningDate = in.readString();
        endingDate = in.readString();
        numberOfGuests = in.readInt();
        totalCost = in.readInt();
        bookingStatus = in.readString();
        guestUser = in.readParcelable(User.class.getClassLoader());
        hostUser = in.readParcelable(User.class.getClassLoader());
        guestUserId = in.readString();
        guestName = in.readString();
        hostName = in.readString();
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

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

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
    public User getGuestUser() {
        return guestUser;
    }

    public void setGuestUser(User guestUser) {
        this.guestUser = guestUser;
    }

    public User getHostUser() {
        return hostUser;
    }

    public void setHostUser(User hostUser) {
        this.hostUser = hostUser;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(accommodationId);
        dest.writeString(beginningDate);
        dest.writeString(endingDate);
        dest.writeInt(numberOfGuests);
        dest.writeInt(totalCost);
        dest.writeString(bookingStatus);
        dest.writeParcelable(guestUser, flags);
        dest.writeParcelable(hostUser, flags);
        dest.writeString(guestUserId);
        dest.writeString(guestName);
        dest.writeString(hostName);
    }
}

