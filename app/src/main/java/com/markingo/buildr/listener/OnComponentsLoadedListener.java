package com.markingo.buildr.listener;

import com.markingo.buildr.model.Component;

import java.util.List;

/**
 * Interface for component loading callback
 */
public interface OnComponentsLoadedListener {
    void onComponentsLoaded(List<Component> components);
    void onComponentLoaded(Component component);
    void onError(String errorMessage);
} 