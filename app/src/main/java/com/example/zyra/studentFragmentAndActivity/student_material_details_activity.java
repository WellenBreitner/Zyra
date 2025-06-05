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

import com.example.zyra.R;
import com.example.zyra.modelData.material;
import com.google.android.material.button.MaterialButton;

public class student_material_details_activity extends AppCompatActivity  {

    TextView materialName,materialPostDate,materialDesc;
    ImageButton backButton;
    MaterialButton joinClassButton;
    material item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_material_details);

        initializeUI();
        initializeListener();


    }

    private void initializeListener() {
        backButton.setOnClickListener(view -> backButtonOnclick());
        joinClassButton.setOnClickListener(view -> joinClassOnCLick(item.getMaterialMeetLink()));
    }

    private void initializeUI() {
        materialName = findViewById(R.id.materialDetailsName);
        materialPostDate = findViewById(R.id.materialPostDate);
        materialDesc = findViewById(R.id.materialClassDesc);
        backButton = findViewById(R.id.materialDetailsBackButton);
        joinClassButton = findViewById(R.id.joinClassButton);

        item = (material) getIntent().getSerializableExtra("material_details");

        materialName.setText(item.getMaterialName());
        materialPostDate.setText(item.getMaterialPostDate());
        materialDesc.setText(item.getMaterialDesc());
    }

    private void joinClassOnCLick(String meetLink) {
        try{
            if (item.getMaterialMeetLink().isEmpty()){
                Toast.makeText(this, "No Meeting Link Available", Toast.LENGTH_SHORT).show();
            }else{
                Uri classLink = Uri.parse(meetLink);
                Intent callIntent = new Intent(Intent.ACTION_DEFAULT, classLink);
                startActivity(callIntent);
            }
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "Meeting Link Wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private void backButtonOnclick() {
        finish();
    }
}