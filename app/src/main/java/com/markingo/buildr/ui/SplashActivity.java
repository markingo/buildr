package com.markingo.buildr.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.markingo.buildr.R;
import com.markingo.buildr.ui.auth.LoginActivity;

/**
 * Splash screen with logo animation
 * Also checks if user is already logged in
 */
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DURATION = 2000; // 2 seconds
    
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Initialize Firebase first thing
        initializeFirebase();

        // Animate logo
        animateLogo();

        // Delay navigation
        new Handler(Looper.getMainLooper()).postDelayed(this::navigateToNextScreen, SPLASH_DURATION);
    }
    
    private void initializeFirebase() {
        try {
            FirebaseApp.initializeApp(this);
            firebaseAuth = FirebaseAuth.getInstance();
            // Initialize Firestore to ensure it's ready
            FirebaseFirestore.getInstance();
            Log.d(TAG, "Firebase initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase: " + e.getMessage(), e);
        }
    }
    
    private void animateLogo() {
        ImageView logoImageView = findViewById(R.id.iv_logo);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoImageView.startAnimation(fadeInAnimation);
    }

    /**
     * Navigate to appropriate screen based on login status
     */
    private void navigateToNextScreen() {
        Intent intent;
        
        if (isUserLoggedIn()) {
            // User is already logged in, go to main
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            // User is not logged in, go to login
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }
        
        startActivity(intent);
        
        // Close splash activity to prevent going back
        finish();
    }
    
    private boolean isUserLoggedIn() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser != null;
    }
} 