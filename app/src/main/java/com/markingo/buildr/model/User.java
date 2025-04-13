package com.markingo.buildr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User class representing a user of the application
 */
public class User implements Serializable {
    private String id; // Firebase Auth UID
    private String email;
    private String displayName;
    private String profileImageUrl;
    private Date createdAt;
    private Date lastLoginAt;
    private List<String> favoriteConfigurationIds;
    private boolean premiumUser;

    public User() {
        // Empty constructor for Firebase
        this.favoriteConfigurationIds = new ArrayList<>();
    }

    public User(String id, String email, String displayName) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.createdAt = new Date();
        this.lastLoginAt = new Date();
        this.favoriteConfigurationIds = new ArrayList<>();
        this.premiumUser = false;
    }

    public void addFavoriteConfiguration(String configId) {
        if (favoriteConfigurationIds == null) {
            favoriteConfigurationIds = new ArrayList<>();
        }
        
        if (!favoriteConfigurationIds.contains(configId)) {
            favoriteConfigurationIds.add(configId);
        }
    }

    public void removeFavoriteConfiguration(String configId) {
        if (favoriteConfigurationIds != null) {
            favoriteConfigurationIds.remove(configId);
        }
    }

    public boolean isFavorite(String configId) {
        return favoriteConfigurationIds != null && favoriteConfigurationIds.contains(configId);
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public List<String> getFavoriteConfigurationIds() {
        return favoriteConfigurationIds;
    }

    public void setFavoriteConfigurationIds(List<String> favoriteConfigurationIds) {
        this.favoriteConfigurationIds = favoriteConfigurationIds;
    }

    public boolean isPremiumUser() {
        return premiumUser;
    }

    public void setPremiumUser(boolean premiumUser) {
        this.premiumUser = premiumUser;
    }
} 