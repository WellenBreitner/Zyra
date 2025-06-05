package com.example.zyra.adminFragmentAndActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zyra.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class admin_edit_material_activity extends AppCompatActivity {

    TextInputEditText editMaterialName,editMaterialDesc,editMaterialMeetLink;
    MaterialButton cancelEditMaterialButton, editMaterialButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_material);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        initializeUI();
        initializeListener();
    }

    private void initializeListener() {
        String materialId = getIntent().getStringExtra("material_id");
        editMaterialButton.setOnClickListener(view -> {
            editMaterial(editMaterialName.getText().toString(),
                    editMaterialDesc.getText().toString(),
                    editMaterialMeetLink.getText().toString(),
                    materialId);
        });

        cancelEditMaterialButton.setOnClickListener(view -> {
            finish();
        });
    }

    private void initializeUI() {
        editMaterialName = findViewById(R.id.editMaterialNameEditText);
        editMaterialDesc = findViewById(R.id.editMaterialDescEditText);
        editMaterialMeetLink = findViewById(R.id.editMaterialMeetLinkEditText);
        cancelEditMaterialButton = findViewById(R.id.cancelEditMaterialButton);
        editMaterialButton = findViewById(R.id.editMaterialButton);

    }

    private void editMaterial(String materialName,
                              String materialDesc,
                              String materialMeetLink,
                              String materialId) {
        if (materialName.isEmpty() && materialDesc.isEmpty() && materialMeetLink.isEmpty()){
            editMaterialDesc.setError("Please edit at least one field to update the event.");
            editMaterialMeetLink.setError("Please edit at least one field to update the event.");
            editMaterialName.requestFocus();
        }else{
            DatabaseReference materialRef = FirebaseDatabase.getInstance().getReference("materials").child(materialId);
            boolean isUpdated = false;
            if (!(materialName.isEmpty())) {
                materialRef.child("materialName").setValue(materialName);
                isUpdated = true;
            }
            if (!(materialDesc.isEmpty())) {
                materialRef.child("materialDesc").setValue(materialDesc);
                isUpdated = true;
            }
            if (!(materialMeetLink.isEmpty())) {
                materialRef.child("materialMeetLink").setValue(materialMeetLink);
                isUpdated = true;
            }

            if (isUpdated){
                Toast.makeText(admin_edit_material_activity.this, "Event edited successfully", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(admin_edit_material_activity.this, "Event doesn't edited", Toast.LENGTH_SHORT).show();
            }
        }
    }
}