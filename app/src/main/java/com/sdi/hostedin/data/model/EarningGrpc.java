package com.sdi.hostedin.data.model;

public class EarningGrpc {

    private String month;
    private double earning;

    public EarningGrpc(String month, double earning) {
        this.month = month;
        this.earning = earning;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getEarning() {
        return earning;
    }

    public void setEarning(double earning) {
        this.earning = earning;
    }
}
