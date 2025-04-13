package com.markingo.buildr.model;

/**
 * Enum for component types
 */
public enum ComponentType {
    CPU("cpu"),
    GPU("gpu"),
    RAM("ram"),
    MOTHERBOARD("motherboard"),
    STORAGE("storage"),
    PSU("psu"),
    CASE("case"),
    CPU_COOLER("cpu_cooler"),
    GENERIC("generic");
    
    private final String value;
    
    ComponentType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }
} 