package com.sdi.hostedin.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class NewPasswordRecovery implements Parcelable {
    private String newPassword;
    private String email;

    public NewPasswordRecovery(String newPassword, String email) {
        this.newPassword = newPassword;
        this.email = email;
    }

    protected NewPasswordRecovery(Parcel in) {
        newPassword = in.readString();
        email = in.readString();
    }

    public static final Creator<NewPasswordRecovery> CREATOR = new Creator<NewPasswordRecovery>() {
        @Override
        public NewPasswordRecovery createFromParcel(Parcel in) {
            return new NewPasswordRecovery(in);
        }

        @Override
        public NewPasswordRecovery[] newArray(int size) {
            return new NewPasswordRecovery[size];
        }
    };

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(newPassword);
        dest.writeString(email);
    }
}
