package com.markingo.buildr.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to check network connectivity
 */
public class NetworkUtils {
    private static final String TAG = "NetworkUtils";

    /**
     * Check if the device has an active internet connection
     * @param context Context to access system services
     * @return true if connected, false otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) return false;
            
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        } else {
            @SuppressWarnings("deprecation")
            android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }
    
    /**
     * Check if Firebase is reachable
     */
    public static void checkFirebaseConnectivity(Context context, OnSuccessListener<Boolean> listener) {
        if (!isNetworkAvailable(context)) {
            if (listener != null) {
                listener.onSuccess(false);
            }
            return;
        }
        
        // Use Firestore to check connectivity by getting server timestamp
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", new Date());
        
        firestore.collection("status")
                .document("connection_test")
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    if (listener != null) {
                        listener.onSuccess(true);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onSuccess(false);
                    }
                });
    }
    
    /**
     * Performs a deeper internet connectivity check by trying to connect to a reliable server
     * @param callback Callback with the result
     */
    public static void hasActualInternetConnection(InternetConnectionCallback callback) {
        if (callback == null) return;
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        
        executor.execute(() -> {
            boolean isConnected = false;
            HttpURLConnection connection = null;
            try {
                // Connect to Google's homepage - a reliable site
                URL url = new URL("https://www.google.com");
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.connect();
                isConnected = connection.getResponseCode() == 200;
            } catch (IOException e) {
                Log.e(TAG, "Internet connection error: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                boolean finalIsConnected = isConnected;
                handler.post(() -> callback.onResult(finalIsConnected));
            }
        });
    }
    
    public interface InternetConnectionCallback {
        void onResult(boolean isConnected);
    }
} 