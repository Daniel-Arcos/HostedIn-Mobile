package com.sdi.hostedin.data.model;

public class AccommodationGrpc {
    private String title;
    private int bookingsNumber;
    private double rate;

    public AccommodationGrpc() {
    }

    public AccommodationGrpc(String title, int bookingsNumber, double rate) {
        this.title = title;
        this.bookingsNumber = bookingsNumber;
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBookingsNumber() {
        return bookingsNumber;
    }

    public void setBookingsNumber(int bookingsNumber) {
        this.bookingsNumber = bookingsNumber;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
