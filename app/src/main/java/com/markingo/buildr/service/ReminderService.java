package com.markingo.buildr.service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.markingo.buildr.R;
import com.markingo.buildr.receiver.AlarmReceiver;
import com.markingo.buildr.util.NotificationUtils;

import java.util.Calendar;

/**
 * Service for scheduling reminders with AlarmManager
 */
public class ReminderService {
    private static final String TAG = "ReminderService";
    private static final int REMINDER_REQUEST_CODE = 100;

    /**
     * Check if we have permission to schedule exact alarms
     */
    public static boolean canScheduleExactAlarms(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            return alarmManager.canScheduleExactAlarms();
        }
        return true; // Permission not required for Android 11 and below
    }

    /**
     * Request permission to schedule exact alarms
     * This will direct user to the system settings page
     */
    public static void requestExactAlarmPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Toast.makeText(context, 
                R.string.alarm_permission_required, 
                Toast.LENGTH_LONG).show();
            
            Log.d(TAG, "Requesting SCHEDULE_EXACT_ALARM permission");
            
            try {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Error opening alarm settings: " + e.getMessage());
                Toast.makeText(context, 
                    "Please enable alarm permission in Settings > Apps > BuildR > Permissions", 
                    Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Schedule daily reminder using AlarmManager
     * Will only schedule if we have permission
     */
    public static void scheduleDailyReminder(Context context) {
        try {
            // Check if we have permission to schedule exact alarms
            if (!canScheduleExactAlarms(context)) {
                Log.d(TAG, "Cannot schedule exact alarms, requesting permission");
                requestExactAlarmPermission(context);
                return;
            }

            // Create intent for the alarm receiver
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    REMINDER_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // Set alarm time (9:00 AM by default)
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            // If time has already passed today, schedule for tomorrow
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            // Get alarm manager and schedule
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            pendingIntent
                    );
                    Log.d(TAG, "Daily reminder scheduled for " + calendar.getTime());
                } else {
                    alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            pendingIntent
                    );
                    Log.d(TAG, "Daily reminder scheduled for " + calendar.getTime());
                }
            } else {
                Log.e(TAG, "AlarmManager service is null");
            }
        } catch (SecurityException se) {
            Log.e(TAG, "Security exception when scheduling alarm: " + se.getMessage(), se);
            requestExactAlarmPermission(context);
        } catch (Exception e) {
            Log.e(TAG, "Error scheduling reminder: " + e.getMessage(), e);
        }
    }
} 