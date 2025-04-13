package com.markingo.buildr.ui.component;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.markingo.buildr.R;
import com.markingo.buildr.model.Component;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying components in a RecyclerView
 */
public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ComponentViewHolder> {

    private final List<Component> components;
    private final OnComponentClickListener listener;
    private final NumberFormat currencyFormat;

    public ComponentAdapter(List<Component> components, OnComponentClickListener listener) {
        this.components = components;
        this.listener = listener;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    }

    @NonNull
    @Override
    public ComponentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_component, parent, false);
        return new ComponentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComponentViewHolder holder, int position) {
        try {
            Component component = components.get(position);
            holder.bind(component, listener);
        } catch (Exception e) {
            // Log or handle exception
        }
    }

    @Override
    public int getItemCount() {
        return components.size();
    }

    static class ComponentViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivComponent;
        private final TextView tvComponentName;
        private final TextView tvComponentDetails;
        private final TextView tvPrice;

        public ComponentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivComponent = itemView.findViewById(R.id.iv_component);
            tvComponentName = itemView.findViewById(R.id.tv_component_name);
            tvComponentDetails = itemView.findViewById(R.id.tv_component_details);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }

        public void bind(Component component, OnComponentClickListener listener) {
            try {
                // Set component name (brand + model)
                String displayName = (component.getBrand() != null ? component.getBrand() : "Unknown") + " " + 
                        (component.getModel() != null ? component.getModel() : "Model");
                tvComponentName.setText(displayName);
                
                // Set component details (use getDetailString instead of type)
                String details = component.getDetailString();
                tvComponentDetails.setText(details != null ? details : "");
                
                // Set price
                double price = component.getPrice();
                tvPrice.setText(NumberFormat.getCurrencyInstance(Locale.US).format(price));
                
                // Load image if available
                String imageUrl = component.getImageUrl();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    if (imageUrl.startsWith("file:///android_asset/")) {
                        // Load from assets
                        String assetPath = imageUrl.replace("file:///android_asset/", "");
                        try {
                            Glide.with(itemView.getContext())
                                 .load(Uri.parse(imageUrl))
                                 .placeholder(R.drawable.buildr_logo)
                                 .error(R.drawable.buildr_logo)
                                 .into(ivComponent);
                        } catch (Exception e) {
                            // Fallback to logo if asset can't be loaded
                            ivComponent.setImageResource(R.drawable.buildr_logo);
                        }
                    } else {
                        // Load from web URL
                        Glide.with(itemView.getContext())
                                .load(imageUrl)
                                .placeholder(R.drawable.buildr_logo)
                                .into(ivComponent);
                    }
                } else {
                    ivComponent.setImageResource(R.drawable.buildr_logo);
                }
                
                // Set click listener
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onComponentClick(component);
                    }
                });
            } catch (Exception e) {
                // Handle any errors silently to prevent crashes
                tvComponentName.setText("Error displaying component");
            }
        }
    }

    public interface OnComponentClickListener {
        void onComponentClick(Component component);
    }
} 