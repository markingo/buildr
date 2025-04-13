package com.markingo.buildr.model;

import java.util.Locale;

/**
 * CPU component class
 */
public class CPU extends Component {
    private String socket;
    private int cores;
    private int threads;
    private double baseClock;
    private double boostClock;
    private int tdpWatts;
    private boolean hasIntegratedGraphics;

    // Empty constructor for Firebase
    public CPU() {
    }

    public CPU(String id, String brand, String model, double price, String imageUrl) {
        super(id, brand, model, price, imageUrl);
        setType(ComponentType.CPU);
    }

    /**
     * Get the component type
     */
    @Override
    public ComponentType getType() {
        return ComponentType.CPU;
    }

    /**
     * Get a formatted string with the CPU details
     */
    @Override
    public String getDetailString() {
        return String.format(Locale.getDefault(), 
                "%s %s (%d cores/%d threads, %.1f GHz, %s)", 
                getBrand(), getModel(), cores, threads, baseClock, socket);
    }

    // Getters and setters
    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public int getCores() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public double getBaseClock() {
        return baseClock;
    }

    public void setBaseClock(double baseClock) {
        this.baseClock = baseClock;
    }

    public double getBoostClock() {
        return boostClock;
    }

    public void setBoostClock(double boostClock) {
        this.boostClock = boostClock;
    }

    public int getTdpWatts() {
        return tdpWatts;
    }

    public void setTdpWatts(int tdpWatts) {
        this.tdpWatts = tdpWatts;
    }

    public boolean isHasIntegratedGraphics() {
        return hasIntegratedGraphics;
    }

    public void setHasIntegratedGraphics(boolean hasIntegratedGraphics) {
        this.hasIntegratedGraphics = hasIntegratedGraphics;
    }
} 