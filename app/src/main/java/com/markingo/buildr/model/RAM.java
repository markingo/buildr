package com.markingo.buildr.model;

import java.util.Locale;

/**
 * RAM component class
 */
public class RAM extends Component {
    private int capacityGB;
    private int speedMHz;
    private String memoryType; // DDR4, DDR5, etc.
    private int modules; // Number of sticks
    private int casLatency;

    // Empty constructor for Firebase
    public RAM() {
    }

    public RAM(String id, String brand, String model, double price, String imageUrl) {
        super(id, brand, model, price, imageUrl);
        setType(ComponentType.RAM);
    }

    /**
     * Get the component type
     */
    @Override
    public ComponentType getType() {
        return ComponentType.RAM;
    }

    /**
     * Get a formatted string with the RAM details
     */
    @Override
    public String getDetailString() {
        return String.format(Locale.getDefault(), 
                "%s %s (%dGB %s %dMHz, CL%d, %d module%s)", 
                getBrand(), getModel(), capacityGB, memoryType, speedMHz, casLatency, 
                modules, modules > 1 ? "s" : "");
    }

    // Getters and setters
    public int getCapacityGB() {
        return capacityGB;
    }

    public void setCapacityGB(int capacityGB) {
        this.capacityGB = capacityGB;
    }

    public int getSpeedMHz() {
        return speedMHz;
    }

    public void setSpeedMHz(int speedMHz) {
        this.speedMHz = speedMHz;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    public int getModules() {
        return modules;
    }

    public void setModules(int modules) {
        this.modules = modules;
    }

    public int getCasLatency() {
        return casLatency;
    }

    public void setCasLatency(int casLatency) {
        this.casLatency = casLatency;
    }
} 