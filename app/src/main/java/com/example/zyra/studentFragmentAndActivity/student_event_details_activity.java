package com.example.zyra.studentFragmentAndActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zyra.R;
import com.example.zyra.ROOM.EventRoom;
import com.example.zyra.modelData.event;
import com.google.android.material.button.MaterialButton;

public class student_event_details_activity extends AppCompatActivity {

    ImageButton eventDetailsBackButton;
    TextView eventDetailsName,eventDetailsType,eventDetailsClass,eventDetailsDeadLine,eventDetailsDesc;
    MaterialButton submissionLinkButton;
    event item;
    EventRoom eventOffline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_event_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        item = (event) getIntent().getSerializableExtra("event_detail");

        initializeUI();
        initializeListener();
    }

    private void initializeUI() {
        eventDetailsBackButton = findViewById(R.id.eventDetailsBackButton);
        eventDetailsName = findViewById(R.id.eventDetailsName);
        eventDetailsType = findViewById(R.id.eventDetailsType);
        eventDetailsClass = findViewById(R.id.eventDetailsClass);
        eventDetailsDeadLine = findViewById(R.id.eventDetailsDeadLine);
        eventDetailsDesc =  findViewById(R.id.eventDetailsDesc);
        submissionLinkButton = findViewById(R.id.submissionLinkButton);

        eventDetailsName.setText(item.getEventName());
        eventDetailsType.setText(item.getEventType());
        eventDetailsClass.setText(item.getEventClassCode());
        eventDetailsDeadLine.setText(String.format("%s %s", item.getEventDeadlineDate(), item.getEventDeadlineTime()));
        eventDetailsDesc.setText(item.getEventDesc());
    }

    private void initializeListener() {
        eventDetailsBackButton.setOnClickListener(view -> {
            finish();
        });

        submissionLinkButton.setOnClickListener(view -> {
            submitOnClick(item.getEventSubmissionLink());
        });
    }

    private void submitOnClick(String submitLink) {
        try{
            if (item.getEventSubmissionLink().isEmpty()){
                Toast.makeText(this, "No submission Link Available", Toast.LENGTH_SHORT).show();
            }else{
                Uri classLink = Uri.parse(submitLink);
                Intent callIntent = new Intent(Intent.ACTION_DEFAULT, classLink);
                startActivity(callIntent);
            }
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "Submission Link Wrong", Toast.LENGTH_SHORT).show();
        }
    }
}