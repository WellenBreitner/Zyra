package com.example.zyra.studentFragmentAndActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zyra.R;
import com.example.zyra.modelData.Admin;
import com.example.zyra.modelData.student;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class create_account_activity extends AppCompatActivity {

    TextInputEditText userCreateName, userCreateEmail, userCreatePassword, createAdmin;
    RadioGroup createStudentOrAdminAccount;
    RadioButton selectedButton;
    String createAccountFor;
    MaterialButton userCreateAccountButton, loginButton;
    private Uri avatarUri = null;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeUI();
        initializeListener();
        initializeFireBase();

    }

    private void initializeFireBase() {
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");
    }

    private void initializeListener() {
        createStudentOrAdminAccount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectedButton = findViewById(i);
                if (selectedButton != null) {
                    createAccountFor = selectedButton.getText().toString(); // Update createAccountFor
                }
            }
        });

        userCreateAccountButton.setOnClickListener(view -> {
            String name = userCreateName.getText().toString().trim();
            String email = userCreateEmail.getText().toString().trim();
            String password = userCreatePassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                createUser(name, email, password);
            }
        });

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, login_activity.class);
            startActivity(intent);
        });

    }

    private void createUser(String name ,String email, String password) {

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userCreateEmail.setError("Invalid email format");
            return;
        }
        if (password.length() < 6) {
            userCreatePassword.setError("Password must be at least 6 characters");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user !=null){
                            String uid = user.getUid();

                            Uri avatar = avatarUri;
                            if (avatar == null){
                                avatar = Uri.parse("android.resource://com.example.zyra/" + R.drawable.avatar);
                            }

                            switch (createAccountFor){
                                case "Student": {
                                    student student = new student(name, email);
                                    userRef.child(uid).setValue(student)
                                            .addOnCompleteListener(saveTask -> {
                                                if (saveTask.isSuccessful()) {
                                                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    break;
                                }
                                case "Admin":{
                                    Admin admin = new Admin(name, email);
                                    userRef.child(uid).setValue(admin)
                                            .addOnCompleteListener(saveTask -> {
                                                if (saveTask.isSuccessful()) {
                                                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    break;
                                }
                            }

                        }else {
                            Toast.makeText(this, "Account creation failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initializeUI() {
        userCreateName = findViewById(R.id.userCreateNameEditText);
        userCreateEmail = findViewById(R.id.userCreateEmailEditText); ;
        userCreatePassword = findViewById(R.id.userCreatePasswordEditText); ;
        userCreateAccountButton = findViewById(R.id.userCreateAccountButton); ;
        createStudentOrAdminAccount = findViewById(R.id.studentOrAdmin);
        loginButton = findViewById(R.id.button_login); ;

        RadioButton defaultButton = findViewById(createStudentOrAdminAccount.getCheckedRadioButtonId());
        if (defaultButton != null) {
            createAccountFor = defaultButton.getText().toString();
        }
    }
}