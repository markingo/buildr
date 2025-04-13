package com.markingo.buildr.ui.configuration;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.markingo.buildr.R;
import com.markingo.buildr.model.CPU;
import com.markingo.buildr.model.Component;
import com.markingo.buildr.model.ComponentType;
import com.markingo.buildr.model.Configuration;
import com.markingo.buildr.model.GPU;
import com.markingo.buildr.ui.component.ComponentPickerActivity;
import com.markingo.buildr.util.FirestoreUtil;

import java.util.Date;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Activity for creating and editing PC configurations
 */
public class ConfigurationBuilderActivity extends AppCompatActivity {

    private static final int REQUEST_CPU = 100;
    private static final int REQUEST_GPU = 101;
    private static final int REQUEST_RAM = 102;
    private static final int REQUEST_MOTHERBOARD = 103;
    private static final int REQUEST_STORAGE = 104;
    private static final int REQUEST_PSU = 105;
    private static final int REQUEST_CASE = 106;

    private Toolbar toolbar;
    private EditText etConfigName;
    private EditText etConfigDescription;
    private Button btnSaveConfig;
    private ProgressBar progressBar;
    private TextView tvComponents;
    private Button btnAddCpu;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String currentUserId;
    private Configuration configuration;
    private String configId;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_builder);
        
        // Ensure proper window insets handling
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        
        // Check user login
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            finish();
            return;
        }
        currentUserId = currentUser.getUid();

        // Initialize UI
        initializeUI();
        
        // Check if we're editing an existing configuration
        configId = getIntent().getStringExtra("configurationId");
        if (configId != null && !configId.isEmpty()) {
            isEditMode = true;
            loadConfiguration(configId);
        } else {
            // Creating a new configuration
            configuration = new Configuration("", currentUserId, "", "");
            configuration.setCreatedAt(new Timestamp(new Date()));
            configuration.setUpdatedAt(new Timestamp(new Date()));
        }
    }

    private void initializeUI() {
        toolbar = findViewById(R.id.toolbar);
        
        // Only set support action bar if we don't already have one
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        }
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.new_configuration);
        }

        etConfigName = findViewById(R.id.et_config_name);
        etConfigDescription = findViewById(R.id.et_config_description);
        btnSaveConfig = findViewById(R.id.btn_save_configuration);
        progressBar = findViewById(R.id.progressBar);
        tvComponents = findViewById(R.id.tv_components_title);

        // Set proper button text for components
        btnAddCpu = findViewById(R.id.btn_add_cpu);
        if (btnAddCpu != null) {
            btnAddCpu.setText(getString(R.string.add_component, getString(R.string.cpu)));
            btnAddCpu.setOnClickListener(v -> launchComponentPicker(ComponentType.CPU, REQUEST_CPU));
        }

        // Add button for GPU if it exists in layout
        Button btnAddGpu = findViewById(R.id.btn_add_gpu);
        if (btnAddGpu != null) {
            btnAddGpu.setText(getString(R.string.add_component, getString(R.string.gpu)));
            btnAddGpu.setOnClickListener(v -> launchComponentPicker(ComponentType.GPU, REQUEST_GPU));
        }

        // Add button for RAM if it exists in layout
        Button btnAddRam = findViewById(R.id.btn_add_ram);
        if (btnAddRam != null) {
            btnAddRam.setText(getString(R.string.add_component, getString(R.string.ram)));
            btnAddRam.setOnClickListener(v -> launchComponentPicker(ComponentType.RAM, REQUEST_RAM));
        }

        btnSaveConfig.setOnClickListener(v -> saveConfiguration());
    }
    
    private void launchComponentPicker(ComponentType componentType, int requestCode) {
        try {
            Intent intent = new Intent(this, ComponentPickerActivity.class);
            intent.putExtra("componentType", componentType.name());
            startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            Toast.makeText(this, "Error launching component picker: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        try {
            if (resultCode == RESULT_OK && data != null) {
                String componentId = data.getStringExtra("componentId");
                String componentName = data.getStringExtra("componentName");
                if (componentId == null || componentId.isEmpty()) {
                    Toast.makeText(this, "Component ID is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (configuration == null) {
                    configuration = new Configuration("", currentUserId, "", "");
                    configuration.setCreatedAt(new Timestamp(new Date()));
                    configuration.setUpdatedAt(new Timestamp(new Date()));
                }
                
                double componentPrice = data.getDoubleExtra("componentPrice", 0.0);
                int componentWattage = data.getIntExtra("componentWattage", 0);
                
                switch (requestCode) {
                    case REQUEST_CPU:
                        configuration.setCpuId(componentId);
                        configuration.addComponent("cpu", createTempComponent(componentId, componentName, componentPrice, componentWattage));
                        Toast.makeText(this, "CPU added to configuration", Toast.LENGTH_SHORT).show();
                        break;
                    case REQUEST_GPU:
                        configuration.setGpuId(componentId);
                        configuration.addComponent("gpu", createTempComponent(componentId, componentName, componentPrice, componentWattage));
                        Toast.makeText(this, "GPU added to configuration", Toast.LENGTH_SHORT).show();
                        break;
                    case REQUEST_RAM:
                        configuration.setRamId(componentId);
                        configuration.addComponent("ram", createTempComponent(componentId, componentName, componentPrice, componentWattage));
                        Toast.makeText(this, "RAM added to configuration", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Unknown component type", Toast.LENGTH_SHORT).show();
                        return;
                }
                
                // Update price and wattage calculations
                configuration.calculateTotalPrice();
                
                updateComponentsDisplay();
                updatePriceAndWattageDisplay();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error adding component: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
        }
    }
    
    private Component createTempComponent(String id, String displayName, double price, int wattage) {
        // Create a temporary component to display in the configuration
        Component component = new Component() {
            @Override
            public String getDetailString() {
                return displayName != null ? displayName : "Unknown component";
            }
        };
        component.setId(id);
        component.setPrice(price);
        
        // Set wattage for all component types using specs map
        component.addSpec("tdpWatts", wattage);
        
        // Also set specific fields for known component types
        if (component instanceof CPU) {
            ((CPU) component).setTdpWatts(wattage);
        } else if (component instanceof GPU) {
            ((GPU) component).setTdpWatts(wattage);
        }
        
        if (displayName != null) {
            String[] parts = displayName.split(" ", 2);
            if (parts.length > 1) {
                component.setBrand(parts[0]);
                component.setModel(parts[1]);
            } else {
                component.setBrand("Unknown");
                component.setModel(displayName);
            }
        } else {
            component.setBrand("Unknown");
            component.setModel("Unknown");
        }
        
        return component;
    }

    private void updateComponentsDisplay() {
        if (tvComponents == null) {
            return;
        }
        
        StringBuilder componentsText = new StringBuilder();
        
        // List all components in the configuration
        if (configuration != null) {
            Component cpuComponent = configuration.getComponent("cpu");
            Component gpuComponent = configuration.getComponent("gpu");
            Component ramComponent = configuration.getComponent("ram");
            
            if (cpuComponent != null) {
                componentsText.append("• CPU: ").append(cpuComponent.getDetailString()).append("\n");
            }
            
            if (gpuComponent != null) {
                componentsText.append("• GPU: ").append(gpuComponent.getDetailString()).append("\n");
            }
            
            if (ramComponent != null) {
                componentsText.append("• RAM: ").append(ramComponent.getDetailString()).append("\n");
            }
        }
        
        if (componentsText.length() == 0) {
            tvComponents.setText(R.string.no_components);
        } else {
            tvComponents.setText(componentsText.toString());
        }
    }

    private void updatePriceAndWattageDisplay() {
        TextView tvTotalPrice = findViewById(R.id.tv_total_price);
        TextView tvEstimatedWattage = findViewById(R.id.tv_estimated_wattage);
        ProgressBar powerMeter = findViewById(R.id.power_meter);
        
        if (tvTotalPrice != null && configuration != null) {
            String formattedPrice = NumberFormat.getCurrencyInstance(Locale.US).format(configuration.getTotalPrice());
            tvTotalPrice.setText("Total Price: " + formattedPrice);
        }
        
        if (tvEstimatedWattage != null && configuration != null) {
            int wattage = configuration.calculateEstimatedWattage();
            tvEstimatedWattage.setText("Estimated Wattage: " + wattage + " W");
            
            // Update power meter
            if (powerMeter != null) {
                powerMeter.setProgress(wattage);
                
                // Set color based on wattage
                if (wattage > 750) {
                    powerMeter.setProgressTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_light, null)));
                } else if (wattage > 500) {
                    powerMeter.setProgressTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_orange_light, null)));
                } else {
                    powerMeter.setProgressTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_green_light, null)));
                }
            }
        }
    }

    private void loadConfiguration(String configId) {
        progressBar.setVisibility(View.VISIBLE);
        
        FirestoreUtil.getConfiguration(configId, task -> {
            progressBar.setVisibility(View.GONE);
            
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                // Create configuration from document
                configuration = task.getResult().toObject(Configuration.class);
                
                // Set UI with existing values
                if (configuration != null) {
                    populateUI();
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.edit_configuration);
                    }
                    
                    // Update price and wattage display
                    configuration.calculateTotalPrice();
                    updatePriceAndWattageDisplay();
                }
            } else {
                Toast.makeText(ConfigurationBuilderActivity.this, 
                        getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void populateUI() {
        etConfigName.setText(configuration.getName());
        etConfigDescription.setText(configuration.getDescription());
        
        // Display components already in the configuration
        updateComponentsDisplay();
    }

    private void saveConfiguration() {
        String name = etConfigName.getText().toString().trim();
        String description = etConfigDescription.getText().toString().trim();
        
        if (name.isEmpty()) {
            etConfigName.setError(getString(R.string.field_required));
            return;
        }
        
        // Check if any components have been added
        boolean hasComponents = configuration != null && 
                                (configuration.getCpuId() != null || 
                                 configuration.getGpuId() != null || 
                                 configuration.getRamId() != null);
        
        if (!hasComponents) {
            Toast.makeText(this, "Please add at least one component to your configuration", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Update configuration object
        configuration.setName(name);
        configuration.setDescription(description);
        configuration.setUpdatedAt(new Timestamp(new Date()));
        
        progressBar.setVisibility(View.VISIBLE);
        
        // Save using our utility class
        FirestoreUtil.saveConfiguration(configuration, task -> {
            progressBar.setVisibility(View.GONE);
            
            if (task.isSuccessful()) {
                Toast.makeText(ConfigurationBuilderActivity.this, 
                        isEditMode ? R.string.configuration_updated : R.string.configuration_created, 
                        Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ConfigurationBuilderActivity.this, 
                        "Error: " + (task.getException() != null ? 
                                task.getException().getMessage() : "Unknown error"), 
                        Toast.LENGTH_SHORT).show();
            }
        });
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