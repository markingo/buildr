package com.markingo.buildr.model;

/**
 * Generic component implementation for unknown or custom component types
 */
public class GenericComponent extends Component {
    private ComponentType componentType;

    public GenericComponent() {
    }
    
    public GenericComponent(String id, String brand, String model, double price, 
                           String imageUrl, ComponentType type) {
        super(id, brand, model, price, imageUrl);
        this.componentType = type;
    }
    
    @Override
    public ComponentType getType() {
        return componentType;
    }
    
    @Override
    public String getDetailString() {
        return String.format("%s %s", getBrand(), getModel());
    }
} 