package com.markingo.buildr.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.markingo.buildr.model.Configuration;

/**
 * Utility class for Firestore operations with manual permission checks
 */
public class FirestoreUtil {
    private static final String TAG = "FirestoreUtil";
    private static final String CONFIGURATIONS_COLLECTION = "configurations";
    private static final String COMPONENTS_COLLECTION = "components";

    /**
     * Get user configurations with proper permission checks
     */
    public static void getUserConfigurations(OnCompleteListener<QuerySnapshot> listener) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.w(TAG, "Cannot query configurations: User not logged in");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Simplified query that doesn't require a composite index
        db.collection(CONFIGURATIONS_COLLECTION)
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnCompleteListener(listener);
    }

    /**
     * Save a configuration with proper permission checks
     */
    public static void saveConfiguration(Configuration configuration, 
                                         OnCompleteListener<Void> listener) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.w(TAG, "Cannot save configuration: User not logged in");
            return;
        }
        
        // Ensure the userId in the configuration matches the current user
        configuration.setUserId(currentUser.getUid());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef;
        
        if (configuration.getId() == null || configuration.getId().isEmpty()) {
            // New configuration
            docRef = db.collection(CONFIGURATIONS_COLLECTION).document();
            configuration.setId(docRef.getId());
        } else {
            // Existing configuration
            docRef = db.collection(CONFIGURATIONS_COLLECTION).document(configuration.getId());
            
            // Verify ownership before updating
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String documentUserId = document.getString("userId");
                        if (!currentUser.getUid().equals(documentUserId)) {
                            Log.w(TAG, "Cannot update configuration: Permission denied");
                            return;
                        }
                    }
                    
                    // Update the document
                    docRef.set(configuration).addOnCompleteListener(listener);
                }
            });
            return;
        }
        
        // Save new document
        docRef.set(configuration).addOnCompleteListener(listener);
    }

    /**
     * Delete a configuration from Firestore
     * @param configId ID of the configuration to delete
     * @return Task with the result of the deletion
     */
    public static Task<Void> deleteConfiguration(String configId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.w(TAG, "Cannot delete configuration: User not logged in");
            return null;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(CONFIGURATIONS_COLLECTION).document(configId);
        
        // First verify ownership
        Task<DocumentSnapshot> getTask = docRef.get();
        return getTask.continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String documentUserId = document.getString("userId");
                    if (!currentUser.getUid().equals(documentUserId)) {
                        Log.w(TAG, "Cannot delete configuration: Permission denied");
                        throw new SecurityException("Permission denied");
                    }
                    // Proceed with deletion if user is the owner
                    return docRef.delete();
                }
            }
            return null;
        });
    }

    /**
     * Get a configuration by ID with proper permission checks
     */
    public static void getConfiguration(String configId, OnCompleteListener<DocumentSnapshot> listener) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.w(TAG, "Cannot get configuration: User not logged in");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CONFIGURATIONS_COLLECTION).document(configId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String documentUserId = document.getString("userId");
                            if (!currentUser.getUid().equals(documentUserId)) {
                                Log.w(TAG, "Cannot access configuration: Permission denied");
                                return;
                            }
                        }
                        
                        // Pass the result to the original listener
                        listener.onComplete(task);
                    }
                });
    }
} 