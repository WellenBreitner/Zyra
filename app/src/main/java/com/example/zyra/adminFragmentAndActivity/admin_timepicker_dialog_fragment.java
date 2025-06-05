package com.example.zyra.adminFragmentAndActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.zyra.R;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Locale;


public class admin_timepicker_dialog_fragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private final TextView timePicker;

    public admin_timepicker_dialog_fragment(TextView textView) {
        this.timePicker = textView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(requireContext(), this, hour, minute, DateFormat.is24HourFormat(requireContext()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
        this.timePicker.setText(formattedTime);
    }
}
