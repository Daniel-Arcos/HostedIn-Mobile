package com.sdi.hostedin.feature.guest.bookings.accommodationbooking;

import android.os.Parcel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.utils.DateFormatterUtils;

import java.util.Date;
import java.util.List;

public class DateValidatorBookedDates implements CalendarConstraints.DateValidator {
    private List<Booking> bookedDates;
    private Date beginningDate;
    private Date endingDate;

    public DateValidatorBookedDates(List<Booking> bookedDates) {
        this.bookedDates = bookedDates;
    }

    public DateValidatorBookedDates(Date beginningDate, Date endingDate, List<Booking> bookedDates) {
        this.bookedDates = bookedDates;
        this.beginningDate = beginningDate;
        this.endingDate = endingDate;
    }

    public boolean areBookingDatesValid(Date startDate, Date endDate) {
        if (bookedDates != null) {
            for (Booking bookedDate : bookedDates) {
                String startDateString = bookedDate.getBeginningDate();
                String endDateString = bookedDate.getEndingDate();

                if (startDateString != null && endDateString != null) {
                    Date reservedStartDate = DateFormatterUtils.parseStringToDate(startDateString);
                    Date reservedEndDate = DateFormatterUtils.parseStringToDate(endDateString);

                    if (!startDate.after(reservedEndDate) && !endDate.before(reservedStartDate)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean isValid(long date) {
        long currentTimeMillis = MaterialDatePicker.todayInUtcMilliseconds();

        if (bookedDates != null) {

            for (Booking reserved : bookedDates) {
                if (date < currentTimeMillis) {
                    return false;
                }

                if (reserved.getBeginningDate() != null && reserved.getEndingDate() != null) {
                    long beginDate = DateFormatterUtils.parseStringToDate(reserved.getBeginningDate()).getTime();
                    long endDate = DateFormatterUtils.parseStringToDate(reserved.getEndingDate()).getTime();
                    if (date >= beginDate && date <= endDate) {
                        return false;
                    }
                }
            }

            return true;
        } else if (date < currentTimeMillis) {
                return false;
        }

        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(bookedDates);
    }

    public static final Creator<DateValidatorBookedDates> CREATOR = new Creator<DateValidatorBookedDates>() {
        @Override
        public DateValidatorBookedDates createFromParcel(Parcel in) {
            long startDate = in.readLong();
            List<Booking> bookedDates = in.createTypedArrayList(Booking.CREATOR);
            return new DateValidatorBookedDates(bookedDates);
        }

        @Override
        public DateValidatorBookedDates[] newArray(int size) {
            return new DateValidatorBookedDates[size];
        }
    };

}