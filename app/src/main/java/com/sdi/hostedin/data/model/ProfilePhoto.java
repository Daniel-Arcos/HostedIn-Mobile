package com.sdi.hostedin.data.model;

public class ProfilePhoto {
    private String type;
    private byte[] data;

    public String getType() {
        return type;
    }

    public void setType(String subType) {
        this.type = subType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    
}