package com.example.zyra.adminFragmentAndActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zyra.R;
import com.example.zyra.modelData.material;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class admin_add_material_activity extends AppCompatActivity {

    TextInputEditText materialClassName,addMaterialName,addMaterialDesc,addMaterialMeetLink;
    MaterialButton cancelAddMaterialButton, addMaterialButton;
    DatabaseReference classRef, materialRef;
    String materialClassCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_material);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeUI();
        initializeFireBase();
        initializeListener();
    }

    private void initializeFireBase() {
        classRef = FirebaseDatabase.getInstance().getReference("classes");
        materialRef = FirebaseDatabase.getInstance().getReference("materials");
    }
    private void initializeListener() {
        addMaterialButton.setOnClickListener(view -> {

            addMaterial(materialClassCode,
                    addMaterialName.getText().toString(),
                    addMaterialDesc.getText().toString(),
                    addMaterialMeetLink.getText().toString());

        });

        cancelAddMaterialButton.setOnClickListener(view -> {
            finish();
        });
    }

    private void addMaterial(String classCode,
                             String materialName,
                             String materialDesc,
                             String materialMeetLink) {

       if (materialName.isEmpty()) {
            addMaterialName.setError("Material name can't be empty");
            addMaterialName.requestFocus();
        } else if (materialDesc.isEmpty()) {
            addMaterialDesc.setError("Material description can't be empty");
            addMaterialDesc.requestFocus();
        } else if (materialMeetLink.isEmpty()) {
            addMaterialMeetLink.setError("Material link can't be empty");
            addMaterialMeetLink.requestFocus();
        } else {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
            String currentDate = dateFormat.format(calendar.getTime());
            material newMaterial = new material(classCode,materialName,currentDate,materialDesc,materialMeetLink);

            classRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        boolean classCodeExists = false;
                        for (DataSnapshot materialSnapshot : snapshot.getChildren()) {
                            String classCode = materialSnapshot.child("classCode").getValue(String.class);
                            if (classCode != null && classCode.equals(newMaterial.getMaterialClassCode())){
                                classCodeExists = true;
                                break;
                            }
                        }
                        if (classCodeExists){
                            materialRef.child(newMaterial.getMaterialId()).setValue(newMaterial)
                                    .addOnCompleteListener(task -> {
                                        Toast.makeText(admin_add_material_activity.this, "Material added successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e->{
                                        Toast.makeText(admin_add_material_activity.this, "Failed to add Material", Toast.LENGTH_SHORT).show();
                                    });

                            finish();
                        }else{
                            Toast.makeText(admin_add_material_activity.this, "Error: Class code does not exist in subjects", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(admin_add_material_activity.this, "Error checking class code for material", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initializeUI() {
        materialClassName = findViewById(R.id.materialClassNameEditText);
        addMaterialName = findViewById(R.id.addMaterialNameEditText);
        addMaterialDesc = findViewById(R.id.addMaterialDescEditText);
        addMaterialMeetLink = findViewById(R.id.addMaterialMeetLinkEditText);
        cancelAddMaterialButton = findViewById(R.id.cancelAddMaterialButton);
        addMaterialButton = findViewById(R.id.addMaterialButton);

        materialClassCode = getIntent().getStringExtra("material_class_code");

        materialClassName.setText(materialClassCode);
        materialClassName.setEnabled(false);

    }
}