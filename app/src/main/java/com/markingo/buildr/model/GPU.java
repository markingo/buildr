package com.markingo.buildr.model;

import java.util.Locale;

/**
 * GPU component class
 */
public class GPU extends Component {
    private int vramGB;
    private String memoryType;
    private int tdpWatts;
    private String architecture;

    // Empty constructor for Firebase
    public GPU() {
    }

    public GPU(String id, String brand, String model, double price, String imageUrl) {
        super(id, brand, model, price, imageUrl);
        setType(ComponentType.GPU);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.GPU;
    }

    /**
     * Get a formatted string with the GPU details
     */
    @Override
    public String getDetailString() {
        return String.format(Locale.getDefault(),
                "%s %s (%dGB %s)", 
                getBrand(), getModel(), vramGB, memoryType);
    }

    // Getters and setters
    public int getVramGB() {
        return vramGB;
    }

    public void setVramGB(int vramGB) {
        this.vramGB = vramGB;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    public int getTdpWatts() {
        return tdpWatts;
    }

    public void setTdpWatts(int tdpWatts) {
        this.tdpWatts = tdpWatts;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }
} 