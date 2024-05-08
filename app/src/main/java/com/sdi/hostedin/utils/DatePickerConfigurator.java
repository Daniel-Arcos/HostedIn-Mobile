package com.sdi.hostedin.utils;

import android.app.DatePickerDialog;
import android.widget.EditText;

import java.util.Calendar;

public class DatePickerConfigurator {

    public static void configureDatePicker(EditText editTextDate){
        editTextDate.setKeyListener(null);
        editTextDate.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus){
                showDatePickerDialog(editTextDate);
            }
        });
        editTextDate.setOnClickListener(v->{
            showDatePickerDialog(editTextDate);
        });
    }

    public static void showDatePickerDialog(EditText editTextDate){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int maxYear = year - 18;

        // TODO: Show the registered user birthdate
        DatePickerDialog datePickerDialog = new DatePickerDialog(editTextDate.getContext(), (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
            editTextDate.setText(selectedDate);
        }, maxYear, month, day);

        datePickerDialog.getDatePicker().setMaxDate(getMaxDate(maxYear, month, day));

        datePickerDialog.show();
    }

    public static long getMaxDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }
}
