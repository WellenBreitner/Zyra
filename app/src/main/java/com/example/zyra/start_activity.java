package com.example.zyra;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.zyra.adminFragmentAndActivity.admin_container_activity;
import com.example.zyra.studentFragmentAndActivity.login_activity;
import com.example.zyra.studentFragmentAndActivity.student_container_activity;

import java.security.Permission;


public class start_activity extends AppCompatActivity {

    private NotificationManager mNotificationManager;
    private static final String PRIMARY_CHANNEL_ID = "PRIMARY_NOTIFICATION_CHANNEL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);

        Button startButton = findViewById(R.id.startButton);
        Permission();
        createNotificationChannel();
        startButton.setOnClickListener(view -> startButton());

    }

    private void startButton() {
        Intent intent = new Intent(this, login_activity.class);
        startActivity(intent);
    }

    private void createNotificationChannel(){
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){

            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Event reminder notification",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);

            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }


    private void Permission(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityResultLauncher<String[]> requestPermissionLauncher =
                        registerForActivityResult(
                                new ActivityResultContracts.RequestMultiplePermissions(),
                                permission -> {
                                    if (Boolean.TRUE.equals(permission.getOrDefault(Manifest.permission.POST_NOTIFICATIONS, false))) {
                                        Log.d("PERMISSION", "Permission Granted");
                                    } else {
                                        Log.d("PERMISSION", "Permission Denied");
                                    }
                                });

                requestPermissionLauncher.launch(new String[]{Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.SCHEDULE_EXACT_ALARM});
            }
        }
}