package com.sdi.hostedin.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidator {

    private static final String FULL_NAME_REGEX = "^[A-Za-zÀ-ÿ'\\s]{6,250}$";
    private static final String PHONE_NUMBER_REGEX = "^\\d{10}$";
    private static final String OCCUPATION_REGEX = "^[\\w\\s\\d\\S]{4,500}$";
    private static final String RESIDENCE_REGEX = "^[\\w\\s\\d\\S]{4,50}$";

    public static boolean isFullNameValid(String fullName) {
        boolean isValid = false;

        if (fullName != null && !fullName.isEmpty()) {
            Pattern fullNamePattern = Pattern.compile(FULL_NAME_REGEX);
            Matcher matchPattern = fullNamePattern.matcher(fullName);
            isValid = matchPattern.find();
        }

        return isValid;
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            Pattern phoneNumberPattern = Pattern.compile(PHONE_NUMBER_REGEX);
            Matcher matchPattern = phoneNumberPattern.matcher(phoneNumber);
            isValid = matchPattern.find();
        }

        return isValid;
    }

    public static boolean isOccupationValid(String occupation) {
        boolean isValid = false;

        if (occupation != null && !occupation.isEmpty()) {
            Pattern phoneNumberPattern = Pattern.compile(OCCUPATION_REGEX);
            Matcher matchPattern = phoneNumberPattern.matcher(occupation);
            isValid = matchPattern.find();
        }

        return isValid;
    }

    public static boolean isResidenceValid(String stay) {
        boolean isValid = false;

        if (stay != null && !stay.isEmpty()) {
            Pattern phoneNumberPattern = Pattern.compile(RESIDENCE_REGEX);
            Matcher matchPattern = phoneNumberPattern.matcher(stay);
            isValid = matchPattern.find();
        }

        return isValid;
    }







}
