package com.markingo.buildr.model;

/**
 * CPU Cooler component class
 */
public class CPUCooler extends Component {
    private String type; // Air, AIO Liquid, etc.
    private int fanSize; // mm
    private int fanCount;
    private String compatibleSockets; // AM4, LGA1700, etc.
    private int heightMM;
    private int tdpRatingWatts;
    private boolean hasRGB;
    private int noiseLevel; // dB

    public CPUCooler() {
        // Empty constructor for Firebase
    }

    public CPUCooler(String id, String brand, String model, double price, String imageUrl,
                     String type, int fanSize, int fanCount, String compatibleSockets, 
                     int heightMM, int tdpRatingWatts, boolean hasRGB, int noiseLevel) {
        super(id, brand, model, price, imageUrl);
        this.type = type;
        this.fanSize = fanSize;
        this.fanCount = fanCount;
        this.compatibleSockets = compatibleSockets;
        this.heightMM = heightMM;
        this.tdpRatingWatts = tdpRatingWatts;
        this.hasRGB = hasRGB;
        this.noiseLevel = noiseLevel;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.CPU_COOLER;
    }

    @Override
    public String getDetailString() {
        return String.format("%s %s (%s, %dmm, %d fans)", 
                getBrand(), getModel(), type, fanSize, fanCount);
    }

    // Getters and setters
    public String getCoolerType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFanSize() {
        return fanSize;
    }

    public void setFanSize(int fanSize) {
        this.fanSize = fanSize;
    }

    public int getFanCount() {
        return fanCount;
    }

    public void setFanCount(int fanCount) {
        this.fanCount = fanCount;
    }

    public String getCompatibleSockets() {
        return compatibleSockets;
    }

    public void setCompatibleSockets(String compatibleSockets) {
        this.compatibleSockets = compatibleSockets;
    }

    public int getHeightMM() {
        return heightMM;
    }

    public void setHeightMM(int heightMM) {
        this.heightMM = heightMM;
    }

    public int getTdpRatingWatts() {
        return tdpRatingWatts;
    }

    public void setTdpRatingWatts(int tdpRatingWatts) {
        this.tdpRatingWatts = tdpRatingWatts;
    }

    public boolean isHasRGB() {
        return hasRGB;
    }

    public void setHasRGB(boolean hasRGB) {
        this.hasRGB = hasRGB;
    }

    public int getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(int noiseLevel) {
        this.noiseLevel = noiseLevel;
    }
} 