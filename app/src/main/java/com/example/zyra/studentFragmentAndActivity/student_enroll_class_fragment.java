package com.example.zyra.studentFragmentAndActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.zyra.R;
import com.example.zyra.modelData.subject;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class student_enroll_class_fragment extends DialogFragment {

    private TextInputEditText classCodeEditText,classPasswordEditText;
    private MaterialButton cancelButton, enrollButton;
    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(container != null){
            container.removeAllViews();
        }
        v = inflater.inflate(R.layout.fragment_student_enroll_class, container, false);

        initializeUI();
        initializeListener();

        return v;
    }

    private void initializeListener() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID;
        if (currentUser == null){
            userID = "";
            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
        }else{
            userID = currentUser.getUid();
        }
        cancelButton.setOnClickListener(view -> cancelEnrollButtonOnclick());
        enrollButton.setOnClickListener(view -> enrollButtonOnClick(
                classCodeEditText.getText().toString().replaceAll(" ", ""),
                classPasswordEditText.getText().toString(),
                userID
        ));
    }

    private void initializeUI() {
        classCodeEditText = v.findViewById(R.id.classCodeEditText);
        classPasswordEditText = v.findViewById(R.id.classPasswordEditText);
        cancelButton = v.findViewById(R.id.cancelEnrollButton);
        enrollButton = v.findViewById(R.id.enrollButton);
    }

    private void enrollButtonOnClick(String classCode, String classPassword, String studentid) {
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference subjectRef = FirebaseDatabase.getInstance().getReference("classes");

        subjectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isClassCodeAndPasswordValid = false;

                if (snapshot.exists()) {
                    for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                        String storedClassCode = subjectSnapshot.child("classCode").getValue(String.class);
                        String storedPassword = subjectSnapshot.child("classPassword").getValue(String.class);

                        if (storedClassCode != null && storedPassword != null
                                && storedClassCode.equals(classCode) && storedPassword.equals(classPassword)) {
                            isClassCodeAndPasswordValid = true;
                            break;
                        }
                    }


                    if (isClassCodeAndPasswordValid) {
                        DatabaseReference studentIdRef = studentRef.child(studentid);
                        studentIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot studentSnapshot) {
                                if (studentSnapshot.exists()) {
                                    List<String> registeredSubjects = studentSnapshot.child("registeredSubjects")
                                            .getValue(new GenericTypeIndicator<List<String>>() {
                                            });

                                    if (registeredSubjects == null) {
                                        registeredSubjects = new ArrayList<>();
                                    }

                                    if (!registeredSubjects.contains(classCode)) {
                                        registeredSubjects.add(classCode);

                                        studentIdRef.child("registeredSubjects").setValue(registeredSubjects).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Successfully registered to the subject.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Failed to register to the subject.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getContext(), "Student already enrolled in this subject.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Student not found.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Incorrect class code or class password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Class code not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

        private void cancelEnrollButtonOnclick() {
        dismiss();
    }
}