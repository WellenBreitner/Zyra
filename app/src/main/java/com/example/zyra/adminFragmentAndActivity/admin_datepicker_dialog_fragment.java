package com.example.zyra.adminFragmentAndActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Locale;


public class admin_datepicker_dialog_fragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private final TextView datePicker;

    public admin_datepicker_dialog_fragment(TextView textView) {
        this.datePicker = textView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String formattedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", day, (month + 1), year);
        this.datePicker.setText(formattedDate);
    }
}
