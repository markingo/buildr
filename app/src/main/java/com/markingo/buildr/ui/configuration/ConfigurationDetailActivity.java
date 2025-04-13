package com.markingo.buildr.ui.configuration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.markingo.buildr.R;
import com.markingo.buildr.model.Configuration;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Activity for viewing PC configuration details
 */
public class ConfigurationDetailActivity extends AppCompatActivity {

    private TextView tvConfigName;
    private TextView tvConfigDescription;
    private TextView tvPrice;
    private TextView tvWattage;
    private TextView tvCompatibility;
    private TextView tvCpu;
    private TextView tvGpu;
    private TextView tvRam;
    private TextView tvMotherboard;
    private TextView tvStorage;
    private TextView tvPsu;
    private TextView tvCase;
    private TextView tvCooler;
    private Button btnEdit;
    private Button btnDelete;
    private Button btnShare;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String configurationId;
    private Configuration configuration;
    private NumberFormat currencyFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_detail);

        // Show back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.app_name);
        }

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        
        // Initialize currency formatter
        currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        // Get configuration ID
        configurationId = getIntent().getStringExtra("configurationId");
        if (configurationId == null) {
            Toast.makeText(this, "No configuration ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI elements
        initializeUI();

        // Set click listeners
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ConfigurationDetailActivity.this, ConfigurationBuilderActivity.class);
            intent.putExtra("configurationId", configurationId);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> deleteConfiguration());

        btnShare.setOnClickListener(v -> shareConfiguration());

        // Load configuration
        loadConfiguration();
    }

    private void initializeUI() {
        tvConfigName = findViewById(R.id.tv_config_name);
        tvConfigDescription = findViewById(R.id.tv_config_description);
        tvPrice = findViewById(R.id.tv_price);
        tvWattage = findViewById(R.id.tv_wattage);
        tvCompatibility = findViewById(R.id.tv_compatibility);
        tvCpu = findViewById(R.id.tv_cpu);
        tvGpu = findViewById(R.id.tv_gpu);
        tvRam = findViewById(R.id.tv_ram);
        tvMotherboard = findViewById(R.id.tv_motherboard);
        tvStorage = findViewById(R.id.tv_storage);
        tvPsu = findViewById(R.id.tv_psu);
        tvCase = findViewById(R.id.tv_case);
        tvCooler = findViewById(R.id.tv_cooler);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);
        btnShare = findViewById(R.id.btn_share);
        progressBar = findViewById(R.id.progressBar);
    }

    private void loadConfiguration() {
        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("configurations").document(configurationId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    progressBar.setVisibility(View.GONE);
                    configuration = documentSnapshot.toObject(Configuration.class);
                    if (configuration != null) {
                        updateUI();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ConfigurationDetailActivity.this, 
                            "Failed to load configuration: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUI() {
        // For milestone purposes, we'll just set the basic information
        // In a real app, we would load all the component details from Firestore
        
        tvConfigName.setText(configuration.getName());
        tvConfigDescription.setText(configuration.getDescription());
        
        // Set total price
        String formattedPrice = currencyFormat.format(configuration.getTotalPrice());
        tvPrice.setText("Total Price: " + formattedPrice);
        
        // Set estimated wattage
        String wattageText = getString(R.string.estimated_wattage, 
                configuration.calculateEstimatedWattage());
        tvWattage.setText(wattageText);
        
        // Set compatibility status
        boolean isCompatible = configuration.checkCompatibility();
        if (isCompatible) {
            tvCompatibility.setText(R.string.compatible);
            tvCompatibility.setTextColor(getResources().getColor(R.color.compatible_color, null));
        } else {
            tvCompatibility.setText(R.string.incompatible);
            tvCompatibility.setTextColor(getResources().getColor(R.color.incompatible_color, null));
        }
        
        // Hide components that aren't selected
        tvCpu.setVisibility(configuration.getCpuId() != null ? View.VISIBLE : View.GONE);
        tvGpu.setVisibility(configuration.getGpuId() != null ? View.VISIBLE : View.GONE);
        tvRam.setVisibility(configuration.getRamId() != null ? View.VISIBLE : View.GONE);
        tvMotherboard.setVisibility(configuration.getMotherboardId() != null ? View.VISIBLE : View.GONE);
        tvStorage.setVisibility(configuration.getStorageIds() != null && !configuration.getStorageIds().isEmpty() ? View.VISIBLE : View.GONE);
        tvPsu.setVisibility(configuration.getPsuId() != null ? View.VISIBLE : View.GONE);
        tvCase.setVisibility(configuration.getCaseId() != null ? View.VISIBLE : View.GONE);
        tvCooler.setVisibility(configuration.getCoolerIds() != null ? View.VISIBLE : View.GONE);
        
        // Only allow editing/deleting if the user owns the configuration
        String currentUserId = firebaseAuth.getCurrentUser().getUid();
        boolean isOwner = configuration.getUserId().equals(currentUserId);
        btnEdit.setVisibility(isOwner ? View.VISIBLE : View.GONE);
        btnDelete.setVisibility(isOwner ? View.VISIBLE : View.GONE);
    }

    private void deleteConfiguration() {
        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("configurations").document(configurationId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ConfigurationDetailActivity.this,
                            R.string.configuration_deleted, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ConfigurationDetailActivity.this,
                            "Error deleting configuration: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void shareConfiguration() {
        if (configuration == null) return;

        // Build a shareable text
        StringBuilder componentsText = new StringBuilder();
        if (configuration.getCpu() != null) {
            componentsText.append("CPU: ").append(configuration.getCpu().getDetailString()).append("\n");
        }
        if (configuration.getGpu() != null) {
            componentsText.append("GPU: ").append(configuration.getGpu().getDetailString()).append("\n");
        }
        if (configuration.getRam() != null) {
            componentsText.append("RAM: ").append(configuration.getRam().getDetailString()).append("\n");
        }
        if (configuration.getMotherboard() != null) {
            componentsText.append("Motherboard: ").append(configuration.getMotherboard().getDetailString()).append("\n");
        }
        
        String shareText = getString(R.string.config_share_text,
                configuration.getName(),
                currencyFormat.format(configuration.getTotalPrice()),
                componentsText.toString());

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.config_share_title));
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 