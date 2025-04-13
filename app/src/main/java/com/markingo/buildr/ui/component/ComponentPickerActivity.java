package com.markingo.buildr.ui.component;

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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.markingo.buildr.R;
import com.markingo.buildr.model.CPU;
import com.markingo.buildr.model.Component;
import com.markingo.buildr.model.ComponentType;
import com.markingo.buildr.model.GPU;
import com.markingo.buildr.model.RAM;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Activity for picking computer components
 */
public class ComponentPickerActivity extends AppCompatActivity {

    private static final String TAG = "ComponentPickerActivity";
    
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextInputEditText etSearch;
    private Button btnFilter;
    private TextView tvNoComponents;
    private ProgressBar progressBar;
    
    private FirebaseFirestore firestore;
    private ComponentType componentType;
    private String componentTypeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_picker);
        
        try {
            // Get component type from intent
            componentTypeStr = getIntent().getStringExtra("componentType");
            if (componentTypeStr == null) {
                Toast.makeText(this, "No component type specified", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            
            try {
                componentType = ComponentType.valueOf(componentTypeStr);
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Invalid component type: " + componentTypeStr, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            
            // Initialize Firebase
            firestore = FirebaseFirestore.getInstance();
            
            // Initialize UI
            initializeUI();
            
            // Set toolbar title based on component type
            String title = getString(R.string.select_component, getString(getComponentNameResId(componentType)));
            toolbar.setTitle(title);
            
            // Set up recycler view
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            
            // Add filter button click listener
            if (btnFilter != null) {
                btnFilter.setOnClickListener(v -> showFilterDialog());
            }
            
            // Load components
            loadComponents();
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing component picker: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            finish();
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
        }
        
        recyclerView = findViewById(R.id.rv_components);
        etSearch = findViewById(R.id.et_search);
        btnFilter = findViewById(R.id.btn_filter);
        tvNoComponents = findViewById(R.id.tv_no_components);
        progressBar = findViewById(R.id.progressBar);
    }
    
    private void loadComponents() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (tvNoComponents != null) {
            tvNoComponents.setVisibility(View.GONE);
        }
        
        try {
            // Create sample components for demo purposes
            List<Component> demoComponents = createSampleComponents(componentType);
            
            // Display components in recycler view
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            
            if (demoComponents.isEmpty()) {
                if (tvNoComponents != null) {
                    tvNoComponents.setVisibility(View.VISIBLE);
                }
            } else {
                if (recyclerView != null) {
                    recyclerView.setAdapter(new ComponentAdapter(demoComponents, this::onComponentSelected));
                }
            }
        } catch (Exception e) {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            Toast.makeText(this, "Error loading components: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
        }
    }
    
    private List<Component> createSampleComponents(ComponentType type) {
        List<Component> components = new ArrayList<>();
        
        try {
            switch (type) {
                case CPU:
                    // Create sample CPUs
                    CPU cpu1 = new CPU();
                    cpu1.setId(UUID.randomUUID().toString());
                    cpu1.setBrand("Intel");
                    cpu1.setModel("Core i7-12700K");
                    cpu1.setPrice(349.99);
                    cpu1.setSocket("LGA1700");
                    cpu1.setCores(12);
                    cpu1.setThreads(20);
                    cpu1.setBaseClock(3.6);
                    cpu1.setTdpWatts(125);
                    cpu1.setType(ComponentType.CPU);
                    components.add(cpu1);
                    
                    CPU cpu2 = new CPU();
                    cpu2.setId(UUID.randomUUID().toString());
                    cpu2.setBrand("AMD");
                    cpu2.setModel("Ryzen 5 5600X");
                    cpu2.setPrice(199.99);
                    cpu2.setSocket("AM4");
                    cpu2.setCores(6);
                    cpu2.setThreads(12);
                    cpu2.setBaseClock(3.7);
                    cpu2.setTdpWatts(65);
                    cpu2.setType(ComponentType.CPU);
                    components.add(cpu2);
                    
                    CPU cpu3 = new CPU();
                    cpu3.setId(UUID.randomUUID().toString());
                    cpu3.setBrand("AMD");
                    cpu3.setModel("Ryzen 7 5800X3D");
                    cpu3.setPrice(299.99);
                    cpu3.setSocket("AM4");
                    cpu3.setCores(8);
                    cpu3.setThreads(16);
                    cpu3.setBaseClock(3.4);
                    cpu3.setTdpWatts(105);
                    cpu3.setType(ComponentType.CPU);
                    components.add(cpu3);
                    break;
                    
                case GPU:
                    // Create sample GPUs
                    GPU gpu1 = new GPU();
                    gpu1.setId(UUID.randomUUID().toString());
                    gpu1.setBrand("NVIDIA");
                    gpu1.setModel("RTX 4070");
                    gpu1.setPrice(599.99);
                    gpu1.setVramGB(12);
                    gpu1.setMemoryType("GDDR6X");
                    gpu1.setTdpWatts(200);
                    gpu1.setType(ComponentType.GPU);
                    components.add(gpu1);
                    
                    GPU gpu2 = new GPU();
                    gpu2.setId(UUID.randomUUID().toString());
                    gpu2.setBrand("AMD");
                    gpu2.setModel("RX 7700 XT");
                    gpu2.setPrice(449.99);
                    gpu2.setVramGB(12);
                    gpu2.setMemoryType("GDDR6");
                    gpu2.setTdpWatts(245);
                    gpu2.setType(ComponentType.GPU);
                    components.add(gpu2);
                    break;
                    
                case RAM:
                    // Create sample RAM modules
                    RAM ram1 = new RAM();
                    ram1.setId(UUID.randomUUID().toString());
                    ram1.setBrand("Corsair");
                    ram1.setModel("Vengeance LPX");
                    ram1.setPrice(74.99);
                    ram1.setCapacityGB(16);
                    ram1.setSpeedMHz(3200);
                    ram1.setMemoryType("DDR4");
                    ram1.setType(ComponentType.RAM);
                    components.add(ram1);
                    
                    RAM ram2 = new RAM();
                    ram2.setId(UUID.randomUUID().toString());
                    ram2.setBrand("G.Skill");
                    ram2.setModel("Trident Z RGB");
                    ram2.setPrice(89.99);
                    ram2.setCapacityGB(32);
                    ram2.setSpeedMHz(3600);
                    ram2.setMemoryType("DDR4");
                    ram2.setType(ComponentType.RAM);
                    components.add(ram2);
                    break;
                    
                // Add more component types as needed
                default:
                    // For other component types, just add a sample component
                    Component genericComponent = new Component() {
                        @Override
                        public String getDetailString() {
                            return getBrand() + " " + getModel();
                        }
                    };
                    genericComponent.setId(UUID.randomUUID().toString());
                    genericComponent.setBrand("Sample Brand");
                    genericComponent.setModel("Sample Model");
                    genericComponent.setPrice(99.99);
                    genericComponent.setType(componentType);
                    components.add(genericComponent);
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error creating components: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
        }
        
        return components;
    }
    
    private void onComponentSelected(Component component) {
        try {
            // Return the selected component to the previous activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("componentId", component.getId());
            resultIntent.putExtra("componentName", component.getBrand() + " " + component.getModel());
            
            // Add price
            resultIntent.putExtra("componentPrice", component.getPrice());
            
            // Add wattage if available
            int wattage = 0;
            if (component instanceof CPU) {
                wattage = ((CPU) component).getTdpWatts();
            } else if (component instanceof GPU) {
                wattage = ((GPU) component).getTdpWatts();
            }
            resultIntent.putExtra("componentWattage", wattage);
            
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error selecting component: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showFilterDialog() {
        // Show filter dialog
        // For milestone, this is a stub
    }
    
    private int getComponentNameResId(ComponentType type) {
        switch (type) {
            case CPU:
                return R.string.cpu;
            case GPU:
                return R.string.gpu;
            case RAM:
                return R.string.ram;
            case MOTHERBOARD:
                return R.string.motherboard;
            case STORAGE:
                return R.string.storage;
            case PSU:
                return R.string.psu;
            case CASE:
                return R.string.case_;
            case CPU_COOLER:
                return R.string.cpu_cooler;
            default:
                return R.string.app_name;
        }
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