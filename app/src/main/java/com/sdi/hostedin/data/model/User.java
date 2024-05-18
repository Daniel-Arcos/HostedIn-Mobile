package com.sdi.hostedin.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable {

    private String _id;

    private String email;

    private String fullName;

    private String birthDate;

    private String phoneNumber;

    private String password;

    private String occupation;

    private String residence;

    private  ProfilePhoto profilePhoto;

    public User() {

    }

    protected User(Parcel in) {
        _id = in.readString();
        email = in.readString();
        fullName = in.readString();
        birthDate = in.readString();
        phoneNumber = in.readString();
        password = in.readString();
        occupation = in.readString();
        residence = in.readString();
        profilePhoto = in.readParcelable(ProfilePhoto.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public ProfilePhoto getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(ProfilePhoto profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(email);
        dest.writeString(fullName);
        dest.writeString(birthDate);
        dest.writeString(phoneNumber);
        dest.writeString(password);
        dest.writeString(occupation);
        dest.writeString(residence);
        dest.writeParcelable(profilePhoto, flags);
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", occupation='" + occupation + '\'' +
                ", residence='" + residence + '\'' +
                '}';
    }
}
