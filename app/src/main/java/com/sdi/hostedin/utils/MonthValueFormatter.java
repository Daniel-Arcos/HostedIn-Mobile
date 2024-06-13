package com.sdi.hostedin.utils;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class MonthValueFormatter extends ValueFormatter {
    private final String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    @Override
    public String getFormattedValue(float value) {
        int index = (int) value - 1;
        if (index >= 0 && index < months.length) {
            return months[index];
        } else {
            return "";
        }
    }
}
