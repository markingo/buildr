package com.markingo.buildr.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration class representing a complete computer build
 */
public class Configuration implements Serializable {
    @DocumentId
    private String id;
    private String userId;
    private String name;
    private String description;
    private String cpuId;
    private String gpuId;
    private String ramId;
    private String motherboardId;
    private List<String> storageIds;
    private String psuId;
    private String caseId;
    private String coolerIds;
    @ServerTimestamp
    private Timestamp createdAt;
    @ServerTimestamp
    private Timestamp updatedAt;
    private double totalPrice;
    private boolean isPublic;

    // Transient fields for component objects
    private transient CPU cpu;
    private transient GPU gpu;
    private transient RAM ram;
    private transient Motherboard motherboard;
    private transient List<Storage> storages;
    private transient PSU psu;
    private transient Case pcCase;
    private transient CPUCooler cpuCooler;

    private Map<String, Component> components;

    public Configuration() {
        // Empty constructor for Firebase
        this.storageIds = new ArrayList<>();
        this.storages = new ArrayList<>();
        components = new HashMap<>();
        totalPrice = 0.0;
    }

    public Configuration(String id, String userId, String name, String description) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.storageIds = new ArrayList<>();
        this.storages = new ArrayList<>();
        this.createdAt = new Timestamp(new Date());
        this.updatedAt = new Timestamp(new Date());
        this.totalPrice = 0.0;
        this.isPublic = false;
        components = new HashMap<>();
    }

    /**
     * Calculate the total price of all components
     */
    public void calculateTotalPrice() {
        double total = 0.0;
        
        if (cpu != null) total += cpu.getPrice();
        if (gpu != null) total += gpu.getPrice();
        if (ram != null) total += ram.getPrice();
        if (motherboard != null) total += motherboard.getPrice();
        if (psu != null) total += psu.getPrice();
        if (pcCase != null) total += pcCase.getPrice();
        if (cpuCooler != null) total += cpuCooler.getPrice();
        
        if (storages != null) {
            for (Storage storage : storages) {
                if (storage != null) {
                    total += storage.getPrice();
                }
            }
        }
        
        // Also check generic components
        if (components != null) {
            for (Component component : components.values()) {
                if (component != null) {
                    total += component.getPrice();
                }
            }
        }
        
        this.totalPrice = total;
    }

    /**
     * Calculate the estimated wattage of the configuration
     */
    public int calculateEstimatedWattage() {
        int totalWattage = 0;
        
        // CPU
        if (cpu != null) {
            totalWattage += cpu.getTdpWatts();
        }
        
        // GPU
        if (gpu != null) {
            totalWattage += gpu.getTdpWatts();
        }
        
        // Add base wattage for other components (rough estimate)
        totalWattage += 50; // Motherboard
        totalWattage += 5;  // RAM
        totalWattage += 10; // Storage drives (per drive)
        totalWattage += 5;  // Case fans
        
        // Add 20% overhead for safety
        return (int)(totalWattage * 1.2);
    }

    /**
     * Check if the configuration components are compatible
     */
    public boolean checkCompatibility() {
        // Check CPU and motherboard socket compatibility
        if (cpu != null && motherboard != null) {
            if (!cpu.getSocket().equals(motherboard.getSocket())) {
                return false;
            }
        }
        
        // Check RAM and motherboard compatibility
        if (ram != null && motherboard != null) {
            if (!ram.getType().equals(motherboard.getMemoryType())) {
                return false;
            }
        }
        
        // Check case and motherboard form factor compatibility
        if (pcCase != null && motherboard != null) {
            if (!pcCase.getSupportedMotherboardFormFactors().contains(motherboard.getFormFactor())) {
                return false;
            }
        }
        
        // Check CPU cooler and CPU socket compatibility
        if (cpuCooler != null && cpu != null) {
            if (!cpuCooler.getCompatibleSockets().contains(cpu.getSocket())) {
                return false;
            }
        }
        
        // Check case and CPU cooler height compatibility
        if (pcCase != null && cpuCooler != null) {
            if (cpuCooler.getHeightMM() > pcCase.getMaxCPUCoolerHeightMM()) {
                return false;
            }
        }
        
        // Check case and GPU length compatibility
        if (pcCase != null && gpu != null) {
            // Assuming GPU has a length field (not added yet)
            // if (gpu.getLengthMM() > pcCase.getMaxGPULengthMM()) {
            //     return false;
            // }
        }
        
        // Check PSU wattage sufficiency
        if (psu != null) {
            int estimatedWattage = calculateEstimatedWattage();
            if (psu.getWattage() < estimatedWattage) {
                return false;
            }
        }
        
        return true;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCpuId() {
        return cpuId;
    }

    public void setCpuId(String cpuId) {
        this.cpuId = cpuId;
    }

    public String getGpuId() {
        return gpuId;
    }

    public void setGpuId(String gpuId) {
        this.gpuId = gpuId;
    }

    public String getRamId() {
        return ramId;
    }

    public void setRamId(String ramId) {
        this.ramId = ramId;
    }

    public String getMotherboardId() {
        return motherboardId;
    }

    public void setMotherboardId(String motherboardId) {
        this.motherboardId = motherboardId;
    }

    public List<String> getStorageIds() {
        return storageIds;
    }

    public void setStorageIds(List<String> storageIds) {
        this.storageIds = storageIds;
    }

    public String getPsuId() {
        return psuId;
    }

    public void setPsuId(String psuId) {
        this.psuId = psuId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCoolerIds() {
        return coolerIds;
    }

    public void setCoolerIds(String coolerIds) {
        this.coolerIds = coolerIds;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    // Transient component getters and setters
    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
        if (cpu != null) {
            this.cpuId = cpu.getId();
        }
    }

    public GPU getGpu() {
        return gpu;
    }

    public void setGpu(GPU gpu) {
        this.gpu = gpu;
        if (gpu != null) {
            this.gpuId = gpu.getId();
        }
    }

    public RAM getRam() {
        return ram;
    }

    public void setRam(RAM ram) {
        this.ram = ram;
        if (ram != null) {
            this.ramId = ram.getId();
        }
    }

    public Motherboard getMotherboard() {
        return motherboard;
    }

    public void setMotherboard(Motherboard motherboard) {
        this.motherboard = motherboard;
        if (motherboard != null) {
            this.motherboardId = motherboard.getId();
        }
    }

    public List<Storage> getStorages() {
        return storages;
    }

    public void setStorages(List<Storage> storages) {
        this.storages = storages;
        if (storages != null) {
            this.storageIds = new ArrayList<>();
            for (Storage storage : storages) {
                if (storage != null) {
                    this.storageIds.add(storage.getId());
                }
            }
        }
    }

    public PSU getPsu() {
        return psu;
    }

    public void setPsu(PSU psu) {
        this.psu = psu;
        if (psu != null) {
            this.psuId = psu.getId();
        }
    }

    public Case getPcCase() {
        return pcCase;
    }

    public void setPcCase(Case pcCase) {
        this.pcCase = pcCase;
        if (pcCase != null) {
            this.caseId = pcCase.getId();
        }
    }

    public CPUCooler getCpuCooler() {
        return cpuCooler;
    }

    public void setCpuCooler(CPUCooler cpuCooler) {
        this.cpuCooler = cpuCooler;
        if (cpuCooler != null) {
            this.coolerIds = cpuCooler.getId();
        }
    }
    
    /**
     * Add a storage device to the configuration
     */
    public void addStorage(Storage storage) {
        if (storage != null) {
            if (this.storages == null) {
                this.storages = new ArrayList<>();
            }
            this.storages.add(storage);
            
            if (this.storageIds == null) {
                this.storageIds = new ArrayList<>();
            }
            this.storageIds.add(storage.getId());
        }
    }
    
    /**
     * Remove a storage device from the configuration
     */
    public void removeStorage(Storage storage) {
        if (storage != null) {
            if (this.storages != null) {
                this.storages.remove(storage);
            }
            
            if (this.storageIds != null) {
                this.storageIds.remove(storage.getId());
            }
        }
    }

    public Map<String, Component> getComponents() {
        return components;
    }

    public void setComponents(Map<String, Component> components) {
        this.components = components;
        calculateTotalPrice();
    }
    
    public void addComponent(String type, Component component) {
        components.put(type, component);
        calculateTotalPrice();
    }
    
    public Component getComponent(String type) {
        return components.get(type);
    }

    public Date getCreatedDate() {
        return createdAt != null ? createdAt.toDate() : null;
    }
    
    public Date getUpdatedDate() {
        return updatedAt != null ? updatedAt.toDate() : null;
    }
} 