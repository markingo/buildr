package com.markingo.buildr.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.markingo.buildr.R;
import com.markingo.buildr.model.Component;
import com.markingo.buildr.model.Configuration;
import com.markingo.buildr.service.NotificationService;
import com.markingo.buildr.service.ReminderService;
import com.markingo.buildr.ui.adapter.ConfigurationAdapter;
import com.markingo.buildr.ui.auth.LoginActivity;
import com.markingo.buildr.ui.configuration.ConfigurationBuilderActivity;
import com.markingo.buildr.ui.configuration.ConfigurationDetailActivity;
import com.markingo.buildr.util.FirestoreUtil;
import com.markingo.buildr.util.NotificationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity displays the user's configurations
 */
public class MainActivity extends AppCompatActivity implements ConfigurationAdapter.ConfigurationClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private LinearLayout emptyStateContainer;
    private TextView tvNoConfigurations;
    private Button btnCreateFirst;
    private ProgressBar progressBar;
    private FloatingActionButton fabAddConfiguration;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseAuth firebaseAuth;
    private ConfigurationAdapter configurationAdapter;
    private List<Configuration> configurationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        initializeUI();

        // Set up RecyclerView
        setupRecyclerView();
        
        // Set up swipe refresh
        setupSwipeRefresh();

        // Set up floating action button
        fabAddConfiguration.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ConfigurationBuilderActivity.class);
            startActivity(intent);
        });
        
        // Set up create first button
        btnCreateFirst.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ConfigurationBuilderActivity.class);
            startActivity(intent);
        });

        // Start notification service
        startNotificationService();

        // Check and request all required permissions
        checkAndRequestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load configurations when activity resumes
        loadConfigurations();
        
        // After returning from permission request, try to schedule again
        if (ReminderService.canScheduleExactAlarms(this)) {
            ReminderService.scheduleDailyReminder(this);
        }
    }

    private void initializeUI() {
        recyclerView = findViewById(R.id.rv_configurations);
        emptyStateContainer = findViewById(R.id.empty_state_container);
        tvNoConfigurations = findViewById(R.id.tv_no_configurations);
        btnCreateFirst = findViewById(R.id.btn_create_first);
        progressBar = findViewById(R.id.progressBar);
        fabAddConfiguration = findViewById(R.id.fab_add_configuration);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
    }

    private void setupRecyclerView() {
        configurationList = new ArrayList<>();
        configurationAdapter = new ConfigurationAdapter(configurationList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(configurationAdapter);
    }
    
    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.primary,
                R.color.secondary,
                R.color.primary_dark
        );
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void loadConfigurations() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            // User not logged in, go to login
            navigateToLogin();
            return;
        }

        if (!swipeRefreshLayout.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }
        
        // Query Firestore for user's configurations using our utility class
        FirestoreUtil.getUserConfigurations(task -> {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            
            if (task.isSuccessful()) {
                configurationList.clear();
                
                for (QueryDocumentSnapshot document : task.getResult()) {
                    try {
                        Configuration configuration = document.toObject(Configuration.class);
                        configurationList.add(configuration);
                    } catch (Exception e) {
                        Log.e(TAG, "Error converting document: " + e.getMessage());
                    }
                }
                
                // Sort by updatedAt timestamp (newest first)
                configurationList.sort((c1, c2) -> {
                    if (c1.getUpdatedAt() == null || c2.getUpdatedAt() == null) {
                        return 0;
                    }
                    return c2.getUpdatedAt().compareTo(c1.getUpdatedAt());
                });
                
                configurationAdapter.notifyDataSetChanged();
                
                // Show/hide no configurations message
                updateEmptyState();
            } else {
                String errorMsg = task.getException() != null ? 
                        task.getException().getMessage() : 
                        getString(R.string.error);
                
                // Show user-friendly error message
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.error_loading) + " " + errorMsg,
                        Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.retry), v -> loadConfigurations()).show();
                
                Log.e(TAG, "Error getting configurations", task.getException());
                updateEmptyState();
            }
        });
    }
    
    // Add overloaded method that accepts a callback
    private void loadConfigurations(Runnable callback) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            // User not logged in, go to login
            navigateToLogin();
            return;
        }

        // Query Firestore for user's configurations using our utility class
        FirestoreUtil.getUserConfigurations(task -> {
            if (task.isSuccessful()) {
                configurationList.clear();
                
                for (QueryDocumentSnapshot document : task.getResult()) {
                    try {
                        Configuration configuration = document.toObject(Configuration.class);
                        configurationList.add(configuration);
                    } catch (Exception e) {
                        Log.e(TAG, "Error converting document: " + e.getMessage());
                    }
                }
                
                // Sort by updatedAt timestamp (newest first)
                configurationList.sort((c1, c2) -> {
                    if (c1.getUpdatedAt() == null || c2.getUpdatedAt() == null) {
                        return 0;
                    }
                    return c2.getUpdatedAt().compareTo(c1.getUpdatedAt());
                });
                
                configurationAdapter.notifyDataSetChanged();
                
                // Show/hide no configurations message
                updateEmptyState();
            } else {
                String errorMsg = task.getException() != null ? 
                        task.getException().getMessage() : 
                        getString(R.string.error);
                
                // Show user-friendly error message
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.error_loading) + " " + errorMsg,
                        Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.retry), v -> loadConfigurations()).show();
                
                Log.e(TAG, "Error getting configurations", task.getException());
                updateEmptyState();
            }
            
            // Execute the callback
            if (callback != null) {
                callback.run();
            }
        });
    }
    
    private void updateEmptyState() {
        if (configurationList.isEmpty()) {
            emptyStateContainer.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyStateContainer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void startNotificationService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_logout) {
            // Sign out user
            firebaseAuth.signOut();
            navigateToLogin();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationClick(Configuration configuration) {
        Intent intent = new Intent(MainActivity.this, ConfigurationDetailActivity.class);
        intent.putExtra("configurationId", configuration.getId());
        startActivity(intent);
    }

    @Override
    public void onEditClick(Configuration configuration) {
        Intent intent = new Intent(MainActivity.this, ConfigurationBuilderActivity.class);
        intent.putExtra("configurationId", configuration.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Configuration configuration) {
        // Create delete confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_confirmation_title)
                .setMessage(getString(R.string.delete_confirmation_message, configuration.getName()))
                .setPositiveButton(R.string.yes, (dialog, id) -> {
                    // Show progress indicator
                    showProgress(true);

                    // Delete the configuration
                    FirestoreUtil.deleteConfiguration(configuration.getId())
                            .addOnSuccessListener(aVoid -> {
                                showProgress(false);
                                Toast.makeText(MainActivity.this, 
                                        getString(R.string.delete_success, configuration.getName()), 
                                        Toast.LENGTH_SHORT).show();
                                loadConfigurations(); // Refresh the list
                            })
                            .addOnFailureListener(e -> {
                                showProgress(false);
                                Toast.makeText(MainActivity.this, 
                                        getString(R.string.delete_error, e.getMessage()), 
                                        Toast.LENGTH_LONG).show();
                            });
                })
                .setNegativeButton(R.string.no, (dialog, id) -> dialog.dismiss());
        
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onShareClick(Configuration configuration) {
        // Create share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.config_share_title));
        
        // Build share text
        StringBuilder componentsText = new StringBuilder();
        
        // Get component information using the component map
        Component cpuComponent = configuration.getComponent("cpu");
        Component gpuComponent = configuration.getComponent("gpu");
        Component ramComponent = configuration.getComponent("ram");
        
        if (cpuComponent != null) {
            componentsText.append("CPU: ").append(cpuComponent.getDetailString()).append("\n");
        }
        if (gpuComponent != null) {
            componentsText.append("GPU: ").append(gpuComponent.getDetailString()).append("\n");
        }
        if (ramComponent != null) {
            componentsText.append("RAM: ").append(ramComponent.getDetailString()).append("\n");
        }
        // Add more components as needed
        
        String shareText = getString(R.string.config_share_text,
                configuration.getName(),
                String.format("$%.2f", configuration.getTotalPrice()),
                componentsText.toString());
        
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Check and request all permissions needed by the app
     */
    private void checkAndRequestPermissions() {
        // Notification permission
        NotificationUtils.requestNotificationPermission(this);
        
        // Exact alarm permission
        if (ReminderService.canScheduleExactAlarms(this)) {
            ReminderService.scheduleDailyReminder(this);
        } else {
            // Request permission if we don't have it
            ReminderService.requestExactAlarmPermission(this);
        }
    }

    /**
     * Finds the position of a configuration in the list
     */
    private int findConfigurationPosition(Configuration configuration) {
        for (int i = 0; i < configurationList.size(); i++) {
            if (configurationList.get(i).getId().equals(configuration.getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onRefresh() {
        loadConfigurations(() -> {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, R.string.refresh_success, Toast.LENGTH_SHORT).show();
        });
    }
} 