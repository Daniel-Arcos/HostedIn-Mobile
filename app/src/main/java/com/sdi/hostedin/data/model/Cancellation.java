package com.sdi.hostedin.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Objects;

public class Cancellation implements Parcelable {

    private String _id;
    private String reason;
    private Date cancellationDate;

    private User cancellator;

    private Booking booking;

    public Cancellation() {
    }

    public Cancellation(String _id, String reason, Date cancellationDate, User cancellator, Booking booking) {
        this._id = _id;
        this.reason = reason;
        this.cancellationDate = cancellationDate;
        this.cancellator = cancellator;
        this.booking = booking;
    }

    public static  final Creator<Cancellation> CREATOR = new Creator<Cancellation>() {
        @Override
        public Cancellation createFromParcel(Parcel in) {
            return new Cancellation(in);
        }

        @Override
        public Cancellation[] newArray(int size) {
            return new Cancellation[size];
        }
    };

    protected Cancellation(Parcel in) {
        _id = in.readString();
        reason = in.readString();
        long cancellationDateLong = in.readLong();
        cancellationDate = new Date(cancellationDateLong);
        cancellator = in.readParcelable(User.class.getClassLoader());
        booking = in.readParcelable(Booking.class.getClassLoader());
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(Date cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public User getCancellator() {
        return cancellator;
    }

    public void setCancellator(User cancellator) {
        this.cancellator = cancellator;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(reason);
        dest.writeLong(cancellationDate != null ? cancellationDate.getTime() : -1);
        dest.writeParcelable(cancellator, flags);
        dest.writeParcelable(booking, flags);
    }


    @Override
    public String toString() {
        return "Cancellation{" +
                "_id='" + _id + '\'' +
                ", reason='" + reason + '\'' +
                ", cancellationDate=" + cancellationDate +
                ", cancellator=" + cancellator +
                ", booking=" + booking +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cancellation that = (Cancellation) o;
        return Objects.equals(_id, that._id) && Objects.equals(reason, that.reason) && Objects.equals(cancellationDate, that.cancellationDate) && Objects.equals(cancellator, that.cancellator) && Objects.equals(booking, that.booking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, reason, cancellationDate, cancellator, booking);
    }
}
