package com.example.zyra.adminFragmentAndActivity;

import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class admin_add_event_activity extends AppCompatActivity {

    TextView timePickerText,datePickerText;
    TextInputEditText addEventNameEditText,addEventClassEditText,addEventDescEditText,addEventSubmissionLinkEditText;
    RadioGroup addEventTypeRadioGroup;
    RadioButton selectedButton;
    MaterialButton cancelAddEventButton,addEventButton;
    DatabaseReference eventRef,classRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeFireBase();
        initializeUI();
        initializeListener();
    }

    private void initializeFireBase() {
        eventRef = FirebaseDatabase.getInstance().getReference("events");
        classRef = FirebaseDatabase.getInstance().getReference("classes");
    }

    private void initializeUI() {
        addEventNameEditText = findViewById(R.id.addEventNameEditText);
        addEventClassEditText = findViewById(R.id.addEventClassEditText);
        addEventDescEditText = findViewById(R.id.addEventDescEditText);
        addEventSubmissionLinkEditText = findViewById(R.id.addEventSubmissionLinkEditText);
        addEventTypeRadioGroup = findViewById(R.id.addEventTypeRadioGroup);
        timePickerText = findViewById(R.id.addDeadlineTimeTimePicker);
        datePickerText = findViewById(R.id.addDeadLineDateDatePicker);
        cancelAddEventButton = findViewById(R.id.cancelAddEventButton);
        addEventButton = findViewById(R.id.addEventButton);
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

        addEventTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectedButton = findViewById(i);
            }
        });

        cancelAddEventButton.setOnClickListener(view -> {
            finish();
        });

        addEventButton.setOnClickListener(view -> {
            String eventType = (selectedButton != null) ? selectedButton.getText().toString() : "";
            String timePicker = !timePickerText.getText().toString().equals("Set time") ? timePickerText.getText().toString() : "";
            String datePicker = !datePickerText.getText().toString().equals("Set date") ? datePickerText.getText().toString() : "";
            addEvent(addEventNameEditText.getText().toString(),
                    addEventClassEditText.getText().toString(),
                    addEventDescEditText.getText().toString(),
                    addEventSubmissionLinkEditText.getText().toString(),
                    eventType,
                    datePicker,
                    timePicker
            );
        });
    }

    private void addEvent(String eventName,
                          String eventClassCode,
                          String eventDesc,
                          String submissionLink,
                          String eventType,
                          String eventDeadlineDate,
                          String eventDeadlineTime) {

        if (eventName.isEmpty()) {
            addEventNameEditText.setError("Event name can't be empty");
            addEventNameEditText.requestFocus();
        } else if (eventClassCode.isEmpty()) {
            addEventClassEditText.setError("Event class can't be empty");
            addEventClassEditText.requestFocus();
        } else if (eventDesc.isEmpty()) {
            addEventDescEditText.setError("Event description can't be empty");
            addEventDescEditText.requestFocus();
        } else if (submissionLink.isEmpty()) {
            addEventSubmissionLinkEditText.setError("Event submission link can't be empty");
            addEventSubmissionLinkEditText.requestFocus();
        } else if (eventType.isEmpty()) {
            Toast.makeText(this, "You must select the event type", Toast.LENGTH_SHORT).show();
        } else if (eventDeadlineDate.isEmpty()) {
            Toast.makeText(this, "Deadline date can't be empty", Toast.LENGTH_SHORT).show();
        } else if (eventDeadlineTime.isEmpty()) {
            Toast.makeText(this, "Deadline time can't be empty", Toast.LENGTH_SHORT).show();
        } else {
            event newEvent = new event(eventName,eventType,eventClassCode,eventDeadlineDate,eventDeadlineTime,eventDesc,submissionLink);

            classRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        boolean classCodeExists = false;
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String classCode = dataSnapshot.child("classCode").getValue(String.class);
                            if (classCode != null && classCode.equals(eventClassCode)) {
                                classCodeExists = true;
                                break;
                            }
                        }
                        if (classCodeExists){
                            eventRef.child(newEvent.getEventId()).setValue(newEvent)
                                    .addOnSuccessListener(task ->{
                                        Toast.makeText(admin_add_event_activity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e->{
                                        Toast.makeText(admin_add_event_activity.this, "Failed to add material", Toast.LENGTH_SHORT).show();
                                    });
                            finish();
                        }else{
                            Toast.makeText(admin_add_event_activity.this, "Error: Class code does not exist in subjects", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(admin_add_event_activity.this, "Error: Class code does not exist in subjects", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(admin_add_event_activity.this, "Error checking class code for event", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}