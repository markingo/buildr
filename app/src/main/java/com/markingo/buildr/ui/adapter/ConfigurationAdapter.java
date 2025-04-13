package com.markingo.buildr.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.markingo.buildr.R;
import com.markingo.buildr.model.Configuration;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying configurations in a RecyclerView
 */
public class ConfigurationAdapter extends RecyclerView.Adapter<ConfigurationAdapter.ConfigurationViewHolder> {

    private final List<Configuration> configurations;
    private final ConfigurationClickListener listener;
    private final NumberFormat currencyFormat;

    public ConfigurationAdapter(List<Configuration> configurations, ConfigurationClickListener listener) {
        this.configurations = configurations;
        this.listener = listener;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    }

    @NonNull
    @Override
    public ConfigurationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_configuration, parent, false);
        return new ConfigurationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigurationViewHolder holder, int position) {
        Configuration configuration = configurations.get(position);
        holder.bind(configuration, listener);
    }

    @Override
    public int getItemCount() {
        return configurations.size();
    }

    /**
     * ViewHolder for configuration items
     */
    public class ConfigurationViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvConfigurationName;
        private final TextView tvPrice;
        private final TextView tvCpu;
        private final TextView tvGpu;
        private final TextView tvRam;
        private final TextView tvStorage;
        private final TextView tvEstimatedWattage;
        private final TextView tvCompatibility;
        private final Button btnEdit;
        private final Button btnShare;
        private final Button btnDelete;

        public ConfigurationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvConfigurationName = itemView.findViewById(R.id.tv_configuration_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvCpu = itemView.findViewById(R.id.tv_cpu);
            tvGpu = itemView.findViewById(R.id.tv_gpu);
            tvRam = itemView.findViewById(R.id.tv_ram);
            tvStorage = itemView.findViewById(R.id.tv_storage);
            tvEstimatedWattage = itemView.findViewById(R.id.tv_estimated_wattage);
            tvCompatibility = itemView.findViewById(R.id.tv_compatibility);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnShare = itemView.findViewById(R.id.btn_share);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(Configuration configuration, ConfigurationClickListener listener) {
            tvConfigurationName.setText(configuration.getName());
            tvPrice.setText(currencyFormat.format(configuration.getTotalPrice()));
            
            // Set component details
            if (configuration.getCpu() != null) {
                tvCpu.setText(String.format("CPU: %s", configuration.getCpu().getDetailString()));
                tvCpu.setVisibility(View.VISIBLE);
            } else {
                tvCpu.setVisibility(View.GONE);
            }
            
            if (configuration.getGpu() != null) {
                tvGpu.setText(String.format("GPU: %s", configuration.getGpu().getDetailString()));
                tvGpu.setVisibility(View.VISIBLE);
            } else {
                tvGpu.setVisibility(View.GONE);
            }
            
            if (configuration.getRam() != null) {
                tvRam.setText(String.format("RAM: %s", configuration.getRam().getDetailString()));
                tvRam.setVisibility(View.VISIBLE);
            } else {
                tvRam.setVisibility(View.GONE);
            }
            
            if (configuration.getStorages() != null && !configuration.getStorages().isEmpty()) {
                StringBuilder storageText = new StringBuilder("Storage: ");
                for (int i = 0; i < configuration.getStorages().size(); i++) {
                    if (i > 0) storageText.append(", ");
                    storageText.append(configuration.getStorages().get(i).getDetailString());
                }
                tvStorage.setText(storageText.toString());
                tvStorage.setVisibility(View.VISIBLE);
            } else {
                tvStorage.setVisibility(View.GONE);
            }
            
            // Set wattage
            tvEstimatedWattage.setText(String.format(
                    itemView.getContext().getString(R.string.estimated_wattage), 
                    configuration.calculateEstimatedWattage()));
            
            // Set compatibility
            boolean isCompatible = configuration.checkCompatibility();
            if (isCompatible) {
                tvCompatibility.setText(R.string.compatible);
                tvCompatibility.setTextColor(itemView.getResources().getColor(R.color.compatible_color, null));
            } else {
                tvCompatibility.setText(R.string.incompatible);
                tvCompatibility.setTextColor(itemView.getResources().getColor(R.color.incompatible_color, null));
            }
            
            // Set click listeners
            itemView.setOnClickListener(v -> listener.onConfigurationClick(configuration));
            btnEdit.setOnClickListener(v -> listener.onEditClick(configuration));
            btnShare.setOnClickListener(v -> listener.onShareClick(configuration));
            btnDelete.setOnClickListener(v -> listener.onDeleteClick(configuration));
        }
    }

    /**
     * Interface for configuration click events
     */
    public interface ConfigurationClickListener {
        void onConfigurationClick(Configuration configuration);
        void onEditClick(Configuration configuration);
        void onShareClick(Configuration configuration);
        void onDeleteClick(Configuration configuration);
    }
} 