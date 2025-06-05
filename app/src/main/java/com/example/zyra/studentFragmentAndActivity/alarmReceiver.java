package com.example.zyra.studentFragmentAndActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.example.zyra.R;

public class alarmReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    private static final String PRIMARY_CHANNEL_ID = "PRIMARY_NOTIFICATION_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        String eventId = intent.getStringExtra("event_id");
        Intent intentToLoginPage = new Intent(context, login_activity.class);
        intentToLoginPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, eventId.hashCode(), intentToLoginPage, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        String eventName = intent.getStringExtra("event_name");
        String eventType = intent.getStringExtra("event_type");
        String eventDate = intent.getStringExtra("event_date");
        String eventTime = intent.getStringExtra("event_time");

        Uri ringtoneUri = student_settings_fragment.getSavedRingtone(context);


        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.zyra)
                .setContentTitle("Event Reminder: " + eventName)
                .setContentText("Don't forget: " + eventName + " " + eventType + " at " + eventDate + " " + eventTime)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(ringtoneUri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        mNotificationManager.notify(eventId.hashCode(), builder.build());
    }
}
