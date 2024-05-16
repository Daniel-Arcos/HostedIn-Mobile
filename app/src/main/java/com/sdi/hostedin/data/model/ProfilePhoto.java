package com.sdi.hostedin.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ProfilePhoto implements Parcelable {
    private String type;
    private byte[] data;

    public  ProfilePhoto() {

    }

    protected ProfilePhoto(Parcel in) {
        type = in.readString();
        data = in.createByteArray();
    }

    public static final Creator<ProfilePhoto> CREATOR = new Creator<ProfilePhoto>() {
        @Override
        public ProfilePhoto createFromParcel(Parcel in) {
            return new ProfilePhoto(in);
        }

        @Override
        public ProfilePhoto[] newArray(int size) {
            return new ProfilePhoto[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeByteArray(data);
    }
}