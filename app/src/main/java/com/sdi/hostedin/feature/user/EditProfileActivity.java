package com.sdi.hostedin.feature.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.ActivityEditProfileBinding;

import java.util.Calendar;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureDatePicker(binding.birthdateTxt);
    }

    private void configureDatePicker(EditText editTextDate){
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

    private void showDatePickerDialog(EditText editTextDate){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int maxYear = year - 18;

        // TODO: Show the registered user birthdate
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
            editTextDate.setText(selectedDate);
        }, maxYear, month, day);

        datePickerDialog.getDatePicker().setMaxDate(getMaxDate(maxYear, month, day));

        datePickerDialog.show();
    }

    private long getMaxDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }
}