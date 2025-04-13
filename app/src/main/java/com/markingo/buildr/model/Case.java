package com.markingo.buildr.model;

/**
 * Case (Computer Chassis) component class
 */
public class Case extends Component {
    private String formFactor; // Mid Tower, Full Tower, etc.
    private String color;
    private String supportedMotherboardFormFactors; // ATX, mATX, ITX, etc.
    private int maxGPULengthMM;
    private int maxCPUCoolerHeightMM;
    private int includedFans;
    private boolean hasRGB;
    private String sidePanelWindow; // Glass, Acrylic, No window

    public Case() {
        // Empty constructor for Firebase
    }

    public Case(String id, String brand, String model, double price, String imageUrl,
                String formFactor, String color, String supportedMotherboardFormFactors,
                int maxGPULengthMM, int maxCPUCoolerHeightMM, int includedFans,
                boolean hasRGB, String sidePanelWindow) {
        super(id, brand, model, price, imageUrl);
        this.formFactor = formFactor;
        this.color = color;
        this.supportedMotherboardFormFactors = supportedMotherboardFormFactors;
        this.maxGPULengthMM = maxGPULengthMM;
        this.maxCPUCoolerHeightMM = maxCPUCoolerHeightMM;
        this.includedFans = includedFans;
        this.hasRGB = hasRGB;
        this.sidePanelWindow = sidePanelWindow;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.CASE;
    }

    @Override
    public String getDetailString() {
        return String.format("%s %s (%s, %s)", 
                getBrand(), getModel(), formFactor, color);
    }

    // Getters and setters
    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSupportedMotherboardFormFactors() {
        return supportedMotherboardFormFactors;
    }

    public void setSupportedMotherboardFormFactors(String supportedMotherboardFormFactors) {
        this.supportedMotherboardFormFactors = supportedMotherboardFormFactors;
    }

    public int getMaxGPULengthMM() {
        return maxGPULengthMM;
    }

    public void setMaxGPULengthMM(int maxGPULengthMM) {
        this.maxGPULengthMM = maxGPULengthMM;
    }

    public int getMaxCPUCoolerHeightMM() {
        return maxCPUCoolerHeightMM;
    }

    public void setMaxCPUCoolerHeightMM(int maxCPUCoolerHeightMM) {
        this.maxCPUCoolerHeightMM = maxCPUCoolerHeightMM;
    }

    public int getIncludedFans() {
        return includedFans;
    }

    public void setIncludedFans(int includedFans) {
        this.includedFans = includedFans;
    }

    public boolean isHasRGB() {
        return hasRGB;
    }

    public void setHasRGB(boolean hasRGB) {
        this.hasRGB = hasRGB;
    }

    public String getSidePanelWindow() {
        return sidePanelWindow;
    }

    public void setSidePanelWindow(String sidePanelWindow) {
        this.sidePanelWindow = sidePanelWindow;
    }
} 