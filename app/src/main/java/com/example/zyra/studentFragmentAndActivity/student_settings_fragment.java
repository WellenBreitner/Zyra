package com.example.zyra.studentFragmentAndActivity;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.zyra.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class student_settings_fragment extends Fragment {

    View v;
    MaterialButton settingAlarmAudioButton;
    private static final String KEY_RINGTONE_URI = "selectedRingtoneUri";
    private static final String PREF_NAME = "AlarmPreferences";
    private ActivityResultLauncher<Intent> ringtoneLauncher;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ringtoneLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedAudioUri = result.getData().getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                        if (selectedAudioUri != null) {
                            saveRingtoneUri(selectedAudioUri);
                        } else {
                            Log.d("sound","No URI selected.");
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container !=null){
            container.removeAllViews();
        }
        v =  inflater.inflate(R.layout.fragment_student_setting, container, false);


        initializeUI();
        initializeListener();

        return v;
    }

    private void initializeUI() {
//        studentEditName = v.findViewById(R.id.editStudentNameEditText);
//        studentEditEmail = v.findViewById(R.id.editStudentEmailEditText);
//        settingEditButton = v.findViewById(R.id.settingStudentSaveButton);
        settingAlarmAudioButton = v.findViewById(R.id.selectSoundForAlarmButton);
    }

    private void initializeListener() {
        settingAlarmAudioButton.setOnClickListener(view -> settingAlarmButtonClick());
    }

    private void settingAlarmButtonClick() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Sound");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        ringtoneLauncher.launch(intent);
    }

    private void saveRingtoneUri(Uri selectedAudioUri) {
        SharedPreferences preferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_RINGTONE_URI, selectedAudioUri.toString());
        editor.apply();
    }

    public static Uri getSavedRingtone(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String ringtoneUriString = preferences.getString(KEY_RINGTONE_URI, null);
        return (ringtoneUriString != null) ? Uri.parse(ringtoneUriString)
                : RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    }

}