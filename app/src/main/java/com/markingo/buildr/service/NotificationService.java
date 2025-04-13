package com.markingo.buildr.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.markingo.buildr.R;
import com.markingo.buildr.util.NotificationUtils;

import java.util.concurrent.TimeUnit;

/**
 * Service to check for new components and send notifications
 */
public class NotificationService extends Service {
    private static final String TAG = "NotificationService";
    private static final long CHECK_INTERVAL = TimeUnit.HOURS.toMillis(12); // Check every 12 hours
    
    private Handler handler;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "NotificationService created");
        
        handler = new Handler(Looper.getMainLooper());
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        
        // Create the notification channel
        NotificationUtils.createNotificationChannel(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "NotificationService started");
        
        // Start periodic check
        startPeriodicCheck();
        
        // If the system kills the service, restart it
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "NotificationService destroyed");
        
        // Remove callbacks to prevent leaks
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * Start periodic component check
     */
    private void startPeriodicCheck() {
        Log.d(TAG, "Starting periodic component check");
        
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkForNewComponents();
                
                // Schedule next check
                handler.postDelayed(this, CHECK_INTERVAL);
            }
        }, CHECK_INTERVAL);
        
        // Also run immediately for the first time
        handler.post(this::checkForNewComponents);
    }

    /**
     * Check for new compatible components
     */
    private void checkForNewComponents() {
        Log.d(TAG, "Checking for new components");
        
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Log.d(TAG, "No user logged in, skipping check");
            return;
        }
        
        // For the milestone, we'll just send a static notification
        // In a real app, we would check the Firestore database for new components
        // that are compatible with the user's existing configurations
        
        String title = getString(R.string.notification_title);
        String message = getString(R.string.component_update_notification);
        NotificationUtils.showNotification(this, title, message);
    }
} 