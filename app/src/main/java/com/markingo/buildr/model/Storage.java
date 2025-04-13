package com.markingo.buildr.model;

/**
 * Storage component class (SSD, HDD, etc.)
 */
public class Storage extends Component {
    private int capacityGB;
    private String type; // SSD, HDD, etc.
    private String formFactor; // 2.5", 3.5", M.2, etc.
    private String interface_; // SATA, NVMe, etc.
    private int readSpeedMBps;
    private int writeSpeedMBps;
    private int cacheMB;
    private double tbw; // Terabytes Written (endurance)

    public Storage() {
        // Empty constructor for Firebase
    }

    public Storage(String id, String brand, String model, double price, String imageUrl,
                  int capacityGB, String type, String formFactor, String interface_,
                  int readSpeedMBps, int writeSpeedMBps, int cacheMB, double tbw) {
        super(id, brand, model, price, imageUrl);
        this.capacityGB = capacityGB;
        this.type = type;
        this.formFactor = formFactor;
        this.interface_ = interface_;
        this.readSpeedMBps = readSpeedMBps;
        this.writeSpeedMBps = writeSpeedMBps;
        this.cacheMB = cacheMB;
        this.tbw = tbw;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.STORAGE;
    }

    @Override
    public String getDetailString() {
        String capacityText = capacityGB >= 1000 ? 
                String.format("%.1fTB", capacityGB/1000.0) : 
                String.format("%dGB", capacityGB);
        
        return String.format("%s %s (%s %s, %s, %s)", 
                getBrand(), getModel(), capacityText, type, formFactor, interface_);
    }

    // Getters and setters
    public int getCapacityGB() {
        return capacityGB;
    }

    public void setCapacityGB(int capacityGB) {
        this.capacityGB = capacityGB;
    }

    public String getStorageType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public String getInterface_() {
        return interface_;
    }

    public void setInterface_(String interface_) {
        this.interface_ = interface_;
    }

    public int getReadSpeedMBps() {
        return readSpeedMBps;
    }

    public void setReadSpeedMBps(int readSpeedMBps) {
        this.readSpeedMBps = readSpeedMBps;
    }

    public int getWriteSpeedMBps() {
        return writeSpeedMBps;
    }

    public void setWriteSpeedMBps(int writeSpeedMBps) {
        this.writeSpeedMBps = writeSpeedMBps;
    }

    public int getCacheMB() {
        return cacheMB;
    }

    public void setCacheMB(int cacheMB) {
        this.cacheMB = cacheMB;
    }

    public double getTbw() {
        return tbw;
    }

    public void setTbw(double tbw) {
        this.tbw = tbw;
    }
} 