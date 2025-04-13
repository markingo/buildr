package com.markingo.buildr.model;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Base component class for PC parts
 */
public class Component implements Serializable {
    
    @DocumentId
    private String id;
    private ComponentType type;
    private String brand;
    private String model;
    private double price;
    private String imageUrl;
    private Map<String, Object> specs;
    
    // Empty constructor required for Firestore
    public Component() {
        specs = new HashMap<>();
    }
    
    // Constructor for generic components
    public Component(ComponentType type, String brand, String model, double price) {
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.specs = new HashMap<>();
    }
    
    // Constructor for all subclasses
    public Component(String id, String brand, String model, double price, String imageUrl) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.imageUrl = imageUrl;
        this.specs = new HashMap<>();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ComponentType getType() {
        return type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, Object> getSpecs() {
        return specs;
    }

    public void setSpecs(Map<String, Object> specs) {
        this.specs = specs;
    }
    
    public void addSpec(String key, Object value) {
        specs.put(key, value);
    }
    
    public Object getSpec(String key) {
        return specs.get(key);
    }
    
    public String getDetailString() {
        return brand + " " + model;
    }
    
    @Override
    public String toString() {
        return brand + " " + model;
    }
} 