package com.sdi.hostedin.utils;

import android.app.DatePickerDialog;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public static void showDatePickerDialog(EditText editTextDate) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int maxYear = currentYear - 18;

        String lastSelectedDate = editTextDate.getText().toString();

        Date date = DateFormatterUtils.parseStringToDate(lastSelectedDate);
        if (date != null) {
            calendar.setTime(date);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(editTextDate.getContext(), (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
            editTextDate.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(getMaxDate(maxYear, currentMonth, currentDay));

        datePickerDialog.show();
    }

    public static long getMaxDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }
}
