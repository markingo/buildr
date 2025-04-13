package com.markingo.buildr.model;

/**
 * Motherboard component class
 */
public class Motherboard extends Component {
    private String socket;
    private String chipset;
    private String formFactor; // ATX, mATX, ITX, etc.
    private int memorySlots;
    private String memoryType; // DDR4, DDR5, etc.
    private int maxMemoryGB;
    private int pciSlots;
    private int m2Slots;
    private int sataConnectors;
    private String wifiSupport;
    private boolean hasRGB;

    public Motherboard() {
        // Empty constructor for Firebase
    }

    public Motherboard(String id, String brand, String model, double price, String imageUrl,
                       String socket, String chipset, String formFactor, int memorySlots,
                       String memoryType, int maxMemoryGB, int pciSlots, int m2Slots,
                       int sataConnectors, String wifiSupport, boolean hasRGB) {
        super(id, brand, model, price, imageUrl);
        this.socket = socket;
        this.chipset = chipset;
        this.formFactor = formFactor;
        this.memorySlots = memorySlots;
        this.memoryType = memoryType;
        this.maxMemoryGB = maxMemoryGB;
        this.pciSlots = pciSlots;
        this.m2Slots = m2Slots;
        this.sataConnectors = sataConnectors;
        this.wifiSupport = wifiSupport;
        this.hasRGB = hasRGB;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.MOTHERBOARD;
    }

    @Override
    public String getDetailString() {
        return String.format("%s %s (%s, %s socket, %s)", 
                getBrand(), getModel(), chipset, socket, formFactor);
    }

    // Getters and setters
    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public String getChipset() {
        return chipset;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public int getMemorySlots() {
        return memorySlots;
    }

    public void setMemorySlots(int memorySlots) {
        this.memorySlots = memorySlots;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    public int getMaxMemoryGB() {
        return maxMemoryGB;
    }

    public void setMaxMemoryGB(int maxMemoryGB) {
        this.maxMemoryGB = maxMemoryGB;
    }

    public int getPciSlots() {
        return pciSlots;
    }

    public void setPciSlots(int pciSlots) {
        this.pciSlots = pciSlots;
    }

    public int getM2Slots() {
        return m2Slots;
    }

    public void setM2Slots(int m2Slots) {
        this.m2Slots = m2Slots;
    }

    public int getSataConnectors() {
        return sataConnectors;
    }

    public void setSataConnectors(int sataConnectors) {
        this.sataConnectors = sataConnectors;
    }

    public String getWifiSupport() {
        return wifiSupport;
    }

    public void setWifiSupport(String wifiSupport) {
        this.wifiSupport = wifiSupport;
    }

    public boolean isHasRGB() {
        return hasRGB;
    }

    public void setHasRGB(boolean hasRGB) {
        this.hasRGB = hasRGB;
    }
} 