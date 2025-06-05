package com.example.zyra.adminFragmentAndActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import com.example.zyra.R;
import com.example.zyra.modelData.event;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class admin_edit_event_activity extends AppCompatActivity {
    TextView timePickerText,datePickerText;
    TextInputEditText editEventNameEditText,editEventClassEditText,editMaterialDescEditText,editMaterialSubmissionLinkEditText;
    RadioGroup editEventTypeRadioGroup;
    RadioButton selectedButton;
    MaterialButton cancelEditEventButton,editEventButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeUI();
        initializeListener();
    }

    private void initializeUI() {
        editEventNameEditText = findViewById(R.id.editEventNameEditText);
        editEventClassEditText = findViewById(R.id.editEventClassEditText);
        editMaterialDescEditText = findViewById(R.id.editMaterialDescEditText);
        editMaterialSubmissionLinkEditText = findViewById(R.id.editMaterialSubmissionLinkEditText);
        editEventTypeRadioGroup = findViewById(R.id.editEventTypeRadioGroup);
        timePickerText = findViewById(R.id.eventDeadlineTimeTimePicker);
        datePickerText = findViewById(R.id.eventDeadLineDateDatePicker);
        cancelEditEventButton = findViewById(R.id.cancelEditEventButton);
        editEventButton = findViewById(R.id.editEventButton);
    }

    private void initializeListener() {
        timePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new admin_timepicker_dialog_fragment(timePickerText);
                dialog.show(getSupportFragmentManager(),"Time picker dialog");
            }
        });

        datePickerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new admin_datepicker_dialog_fragment(datePickerText);
                dialog.show(getSupportFragmentManager(),"date picker dialog");
            }
        });

        editEventTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectedButton = findViewById(i);
            }
        });


        cancelEditEventButton.setOnClickListener(view -> {
            finish();
        });

        editEventButton.setOnClickListener(view -> {
            event intent = (event) getIntent().getSerializableExtra("event_edit");
            String eventType = (selectedButton != null) ? selectedButton.getText().toString() : "";
            String timePicker = !timePickerText.getText().toString().equals("Set time") ? timePickerText.getText().toString() : "";
            String datePicker = !datePickerText.getText().toString().equals("Set date") ? datePickerText.getText().toString() : "";

            Log.d("TimePicker Value", timePicker);
            Log.d("DatePicker Value", datePicker);
            editEvent(editEventNameEditText.getText().toString(),
                    editEventClassEditText.getText().toString(),
                    editMaterialDescEditText.getText().toString(),
                    editMaterialSubmissionLinkEditText.getText().toString(),
                    eventType,
                    datePicker,
                    timePicker,
                    intent.getEventId());
        });


    }

    private void editEvent(String eventName,
                           String eventClassCode,
                           String eventDesc,
                           String submissionLink,
                           String eventType,
                           String eventDeadlineDate,
                           String eventDeadlineTime,
                           String eventId) {

        if (eventName.isEmpty() &&
                eventClassCode.isEmpty() &&
                eventDesc.isEmpty() &&
                submissionLink.isEmpty() &&
                eventType.isEmpty() &&
                eventDeadlineDate.isEmpty() &&
                eventDeadlineTime.isEmpty()) {
            editEventNameEditText.setError("Must select one object to edit");
            editEventClassEditText.setError("Must select one object to edit");
            editMaterialDescEditText.setError("Must select one object to edit");
            editMaterialSubmissionLinkEditText.setError("Must select one object to edit");
            editEventNameEditText.requestFocus();
        } else {
            DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("events").child(eventId);
            DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference("classes");

            boolean isUpdated = false;

            // Update fields directly if they are not empty
            if (!eventName.isEmpty()) {
                eventRef.child("eventName").setValue(eventName);
                isUpdated = true;
            }
            if (!eventDesc.isEmpty()) {
                eventRef.child("eventDesc").setValue(eventDesc);
                isUpdated = true;
            }
            if (!submissionLink.isEmpty()) {
                eventRef.child("eventSubmissionLink").setValue(submissionLink);
                isUpdated = true;
            }
            if (!eventType.isEmpty()) {
                eventRef.child("eventType").setValue(eventType);
                isUpdated = true;
            }
            if (!eventDeadlineDate.isEmpty()) {
                eventRef.child("eventDeadlineDate").setValue(eventDeadlineDate);
                isUpdated = true;
            }
            if (!eventDeadlineTime.isEmpty()) {
                eventRef.child("eventDeadlineTime").setValue(eventDeadlineTime);
                isUpdated = true;
            }

            if (!eventClassCode.isEmpty()) {
                classesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            boolean classCodeExists = false;

                            for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                                String classCode = subjectSnapshot.child("classCode").getValue(String.class);
                                if (classCode != null && classCode.equals(eventClassCode)) {
                                    classCodeExists = true;
                                    break;
                                }
                            }

                            if (classCodeExists) {
                                eventRef.child("eventClass").setValue(eventClassCode);
                                Toast.makeText(admin_edit_event_activity.this, "Event edited successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(admin_edit_event_activity.this, "Error: Class code does not exist in subjects!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(admin_edit_event_activity.this, "Error: Class code does not exist in subjects", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(admin_edit_event_activity.this, "Error checking class code for event", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (isUpdated) {
                Toast.makeText(admin_edit_event_activity.this, "Event edited successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(admin_edit_event_activity.this, "Event was not edited", Toast.LENGTH_SHORT).show();
            }
        }
    }

}