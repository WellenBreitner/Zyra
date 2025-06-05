package com.example.zyra.adminFragmentAndActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.zyra.R;
import com.example.zyra.modelData.subject;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class admin_add_class_activity extends AppCompatActivity {
    MaterialButton addLecturerPhotoButton;
    ImageView image;
    TextInputEditText addClassNameEditText, addClassCodeEditText, addClassPasswordEditText, addClassLecturerEditText;
    Button cancelAddClassButton, addNewClassButton;
    Uri selectedImage;
    ActivityResultLauncher<Intent> getImage;
    private DatabaseReference classRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_add_class);

        getImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImage = result.getData().getData();
                        if (selectedImage != null) {
                            Glide.with(this)
                                    .load(selectedImage)
                                    .into(image);

                        } else {
                            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        initializeUI();
        initializeFireBase();
        initializeListener();

    }

    private void initializeFireBase() {
        classRef = FirebaseDatabase.getInstance().getReference("classes");
    }

    private void initializeUI() {
        addLecturerPhotoButton = findViewById(R.id.addLecturePhotoPicker);
        image = findViewById(R.id.addClassImageView);
        addClassNameEditText = findViewById(R.id.addClassNameEditText);
        addClassCodeEditText = findViewById(R.id.addClassCodeEditText);
        addClassPasswordEditText = findViewById(R.id.addClassPasswordEditText);
        addClassLecturerEditText = findViewById(R.id.addClassLecturerEditText);
        cancelAddClassButton = findViewById(R.id.cancelAddClassButton);
        addNewClassButton = findViewById(R.id.addNewClassButton);
    }

    private void initializeListener() {
        addLecturerPhotoButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            getImage.launch(intent);
        });

        cancelAddClassButton.setOnClickListener(view -> {
            finish();
        });

        addNewClassButton.setOnClickListener(view -> {
            addNewClass(addClassNameEditText.getText().toString(),
                    addClassCodeEditText.getText().toString().replaceAll(" ", ""),
                    addClassPasswordEditText.getText().toString(),
                    addClassLecturerEditText.getText().toString(),
                    selectedImage);
        });
    }


    private void addNewClass(String className,
                             String classCode,
                             String classPassword,
                             String lecturerName,
                             Uri lecturerPhoto) {

        if (className.isEmpty()) {
            addClassNameEditText.setError("Class name can't be empty");
            addClassNameEditText.requestFocus();
        } else if (classCode.isEmpty()) {
            addClassCodeEditText.setError("Class code can't be empty");
            addClassCodeEditText.requestFocus();
        } else if (classPassword.isEmpty()) {
            addClassPasswordEditText.setError("Class password can't be empty");
            addClassPasswordEditText.requestFocus();
        } else if (lecturerName.isEmpty()) {
            addClassLecturerEditText.setError("Lecturer name can't be empty");
            addClassLecturerEditText.requestFocus();
        } else {
            classRef.orderByChild("className").equalTo(className).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        addClassNameEditText.setError("Class name already exists!");
                        addClassNameEditText.requestFocus();
                    } else {
                        classRef.orderByChild("classCode").equalTo(classCode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    addClassCodeEditText.setError("Class code already exists!");
                                    addClassCodeEditText.requestFocus();
                                } else {
                                    Uri avatar = lecturerPhoto;
                                    if (avatar == null) {
                                        avatar = Uri.parse("android.resource://com.example.zyra/" + R.drawable.avatar);
                                    }
                                    subject newSubject = new subject(className, classCode, classPassword, lecturerName,avatar);
                                    classRef.child(newSubject.getSubjectId())
                                            .setValue(newSubject)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(admin_add_class_activity.this, "Class added successfully!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(admin_add_class_activity.this, "Failed to add class: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(admin_add_class_activity.this, "Error checking class code: ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(admin_add_class_activity.this, "Error checking class name ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
