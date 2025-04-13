package com.markingo.buildr;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

/**
 * Application class for BuildR app
 * Initializes Firebase and other app-wide configurations
 */
public class BuildrApplication extends Application {
    
    private static final String TAG = "BuildrApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        
        try {
            // Initialize Firebase
            FirebaseApp.initializeApp(this);
            
            // Configure Firestore settings for offline usage
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                    .build();
            firestore.setFirestoreSettings(settings);
            
            Log.d(TAG, "Firebase initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase: " + e.getMessage(), e);
        }
    }
} 