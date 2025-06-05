package com.example.zyra.studentFragmentAndActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zyra.R;
import com.example.zyra.adminFragmentAndActivity.admin_container_activity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_activity extends AppCompatActivity {

    TextInputEditText userLoginEmail, userLoginPassword;
    MaterialButton userLoginButton, createAccountButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeFirebase();
        initializeUI();
        initializeListener();

    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

    }

    private void initializeUI() {
        userLoginEmail = findViewById(R.id.userLoginEmailEditText);
        userLoginPassword = findViewById(R.id.userLoginPasswordEditText);
        userLoginButton = findViewById(R.id.userLoginButton);
        createAccountButton = findViewById(R.id.button_signup);
    }

    private void initializeListener() {
        userLoginButton.setOnClickListener(view -> {
            String email = userLoginEmail.getText().toString().trim();
            String password = userLoginPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    if (isOnline(this)) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Your offline only can access event page that you download", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(login_activity.this, student_all_downloaded_event_activity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    loginUsers(email, password);
                }
        });

        createAccountButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, create_account_activity.class);
            startActivity(intent);
        });
    }

    private void loginUsers(String email, String password) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkUserStatus(user.getUid());
                            } else {
                                Toast.makeText(this, "Login Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
    }

    private void checkUserStatus(String uid) {
        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String status = snapshot.child("status").getValue(String.class);
                    if (status != null){
                        if (status.equals("Admin")){
                            Intent intent = new Intent(login_activity.this, admin_container_activity.class);
                            startActivity(intent);
                            finish();
                        } else if (status.equals("Student")) {
                            Intent intent = new Intent(login_activity.this, student_container_activity.class);
                            startActivity(intent);
                            finish();
                        } else{
                            Toast.makeText(login_activity.this,"Unknown user role", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(login_activity.this, "User role not found", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(login_activity.this,"User not found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(login_activity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }
        return false;
    }
}