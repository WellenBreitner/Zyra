package com.example.zyra.adminFragmentAndActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.zyra.R;
import com.example.zyra.modelData.subject;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class admin_edit_class_activity extends AppCompatActivity {
    MaterialButton editLecturerPhotoButton,editClassButton,cancelEditClassButton;
    TextInputEditText editClassNameEditText,editClassLecturerEditText;
    ActivityResultLauncher<Intent> getImage;
    Uri selectedImage;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
        initializeListener();

    }

    private void initializeListener() {

        editLecturerPhotoButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            getImage.launch(intent);
        });

        subject intent = (subject) getIntent().getSerializableExtra("edit_class");

        editClassButton.setOnClickListener(view -> {
            editClass(editClassNameEditText.getText().toString(),
                    editClassLecturerEditText.getText().toString(),
                    intent.getSubjectId()
                    );

        });

        cancelEditClassButton.setOnClickListener(view -> {
            finish();
        });



    }

    private void editClass(String className,String lecturerName,String classId) {
        if (className.isEmpty() && lecturerName.isEmpty()){
            editClassNameEditText.setError("Please edit at least one field to update the event.");
            editClassLecturerEditText.setError("Please edit at least one field to update the event.");
            editClassNameEditText.requestFocus();
        }else{

            DatabaseReference classRef = FirebaseDatabase.getInstance().getReference("classes").child(classId);

            if (!(className.isEmpty())) {
                classRef.child("className").setValue(className);
            }
            if (!(lecturerName.isEmpty())) {
                classRef.child("lecturerName").setValue(lecturerName);
            }

            if (selectedImage != null){
                String imageUri = selectedImage.toString();
                classRef.child("lecturerAvatarUrl").setValue(imageUri);
            }
            Toast.makeText(this, "Class edited successfully", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void initializeUI() {
        editClassNameEditText = findViewById(R.id.editClassNameEditText);
        editClassLecturerEditText = findViewById(R.id.editClassLecturerEditText);
        editLecturerPhotoButton = findViewById(R.id.editLecturePhotoPicker);
        image = findViewById(R.id.editClassImageView);
        editClassButton = findViewById(R.id.editClassButton);
        cancelEditClassButton = findViewById(R.id.cancelEditClassButton);
    }

}