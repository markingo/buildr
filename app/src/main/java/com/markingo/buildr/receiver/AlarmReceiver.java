package com.markingo.buildr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.markingo.buildr.service.NotificationService;
import com.markingo.buildr.service.ReminderService;
import com.markingo.buildr.util.NotificationUtils;

/**
 * Receiver for alarm events to show reminders
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Alarm received");
        
        try {
            // Start the notification service to show notification
            Intent serviceIntent = new Intent(context, NotificationService.class);
            context.startService(serviceIntent);
            
            // Show notification directly
            NotificationUtils.showNotification(
                    context, 
                    context.getString(com.markingo.buildr.R.string.notification_title),
                    context.getString(com.markingo.buildr.R.string.component_update_notification)
            );
            
            // Reschedule for tomorrow
            ReminderService.scheduleDailyReminder(context);
        } catch (Exception e) {
            Log.e(TAG, "Error processing alarm: " + e.getMessage(), e);
        }
    }
} 