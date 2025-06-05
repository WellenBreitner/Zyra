package com.example.zyra.studentFragmentAndActivity;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zyra.ROOM.EventRoom;
import com.example.zyra.ROOM.EventViewModel;
import com.example.zyra.R;
import com.example.zyra.studentAdapter.student_offline_events_adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class student_all_downloaded_event_activity extends AppCompatActivity implements student_offline_events_adapter.OnclickItemDownload {

    RecyclerView recyclerView;
    List<EventRoom> items = new ArrayList<>();
    TextView text0;
    private EventViewModel eventViewModel;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_all_downloaded_event);

        initializeUI();
        initializeListener();
    }

    private void initializeListener() {
        backButton.setOnClickListener(view -> notificationBackButtonOnClick());
    }

    private void initializeUI() {
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        text0 = findViewById(R.id.text0);
        eventViewModel.getEventList().observe(this, events -> {
            recyclerView = findViewById(R.id.RVForNotification);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new student_offline_events_adapter(this, events,this));

            if (events.isEmpty()){
                text0.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else{
                text0.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                scheduleEventAlarm(events);
            }
        });

        backButton = findViewById(R.id.offlineEventBackButton);
    }

    private void notificationBackButtonOnClick() {
        finish();
    }

    @Override
    public void itemClicked(EventRoom item) {
        Intent intent = new Intent(this, student_offline_event_detail_activity.class);
        intent.putExtra("downloaded_event_details",item);
        startActivity(intent);
    }

    public void scheduleEventAlarm(List<EventRoom> events){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        for (EventRoom event : events){
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                Date deadline = dateFormat.parse(event.getEventDeadlineDate() + " " + event.getEventDeadlineTime());

                if (deadline != null){
                    long deadlineMillis = deadline.getTime();

                    long DeadlineDue = deadlineMillis;
                    setAlarm(alarmManager,DeadlineDue,event);
                    long twoHourReminder = deadlineMillis - (2 * 60 * 60 * 1000);
                    setAlarm(alarmManager,twoHourReminder,event);
                    long twentyFourHourReminder = deadlineMillis - (24 * 60 * 60 * 1000);
                    setAlarm(alarmManager,twentyFourHourReminder,event);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setAlarm (AlarmManager alarmManager, long timeInMillis, EventRoom event){
        if (timeInMillis >= System.currentTimeMillis()){
            Intent intent = new Intent(this, alarmReceiver.class);
            intent.putExtra("event_id", event.eventId);
            intent.putExtra("event_name",event.getEventName());
            intent.putExtra("event_type",event.getEventType());
            intent.putExtra("event_date",event.getEventDeadlineDate());
            intent.putExtra("event_time",event.getEventDeadlineTime());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,event.eventId.hashCode(),intent,PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                if (alarmManager.canScheduleExactAlarms()){
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,timeInMillis,pendingIntent);
                }else{
                    showExactAlarmPermissionDialog();
                }
            }else{
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,timeInMillis,pendingIntent);
            }

        }
    }

    private void showExactAlarmPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Alarm Permission Require")
                .setMessage("To enable the alarm reminder, this app needs permission from user. Please enable alarm in setting")
                .setPositiveButton("Setting",(dialog,which)->{
                    Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                })
                .setNegativeButton("Cancel",null)
                .show();
    }
}