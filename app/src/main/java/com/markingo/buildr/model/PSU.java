package com.markingo.buildr.model;

/**
 * PSU (Power Supply Unit) component class
 */
public class PSU extends Component {
    private int wattage;
    private String efficiency; // 80+ Bronze, Gold, Platinum, etc.
    private String formFactor; // ATX, SFX, etc.
    private boolean modular; // Full, Semi, No
    private String certification;
    private int fanSize; // mm

    public PSU() {
        // Empty constructor for Firebase
    }

    public PSU(String id, String brand, String model, double price, String imageUrl,
               int wattage, String efficiency, String formFactor, boolean modular,
               String certification, int fanSize) {
        super(id, brand, model, price, imageUrl);
        this.wattage = wattage;
        this.efficiency = efficiency;
        this.formFactor = formFactor;
        this.modular = modular;
        this.certification = certification;
        this.fanSize = fanSize;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.PSU;
    }

    @Override
    public String getDetailString() {
        String modularText = modular ? "Modular" : "Non-modular";
        return String.format("%s %s (%dW, %s, %s, %s)", 
                getBrand(), getModel(), wattage, efficiency, formFactor, modularText);
    }

    // Getters and setters
    public int getWattage() {
        return wattage;
    }

    public void setWattage(int wattage) {
        this.wattage = wattage;
    }

    public String getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public boolean isModular() {
        return modular;
    }

    public void setModular(boolean modular) {
        this.modular = modular;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public int getFanSize() {
        return fanSize;
    }

    public void setFanSize(int fanSize) {
        this.fanSize = fanSize;
    }
} 