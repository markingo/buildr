package com.markingo.buildr.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.markingo.buildr.R;
import com.markingo.buildr.model.Component;
import com.markingo.buildr.model.Configuration;
import com.markingo.buildr.ui.configuration.ConfigurationDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Adapter for displaying Configuration items in the main RecyclerView
 */
public class MainConfigurationAdapter extends RecyclerView.Adapter<MainConfigurationAdapter.ConfigurationViewHolder> {

    private final Context context;
    private List<Configuration> configurations;

    public MainConfigurationAdapter(Context context) {
        this.context = context;
        this.configurations = new ArrayList<>();
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConfigurationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_configuration, parent, false);
        return new ConfigurationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigurationViewHolder holder, int position) {
        Configuration configuration = configurations.get(position);
        holder.bind(configuration);
    }

    @Override
    public int getItemCount() {
        return configurations.size();
    }

    /**
     * ViewHolder for Configuration items
     */
    class ConfigurationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvConfigName;
        private final TextView tvConfigDate;
        private final TextView tvConfigDescription;
        private final TextView tvComponentCount;

        private Configuration configuration;

        public ConfigurationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvConfigName = itemView.findViewById(R.id.tv_config_name);
            tvConfigDate = itemView.findViewById(R.id.tv_config_date);
            tvConfigDescription = itemView.findViewById(R.id.tv_config_description);
            tvComponentCount = itemView.findViewById(R.id.tv_component_count);

            itemView.setOnClickListener(this);
        }

        public void bind(Configuration configuration) {
            this.configuration = configuration;

            tvConfigName.setText(configuration.getName());
            
            // Format updated timestamp
            Timestamp updatedAt = configuration.getUpdatedAt();
            if (updatedAt != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                tvConfigDate.setText(sdf.format(updatedAt.toDate()));
            } else {
                tvConfigDate.setText(R.string.no_date);
            }

            // Set description or placeholder if empty
            String description = configuration.getDescription();
            if (description != null && !description.isEmpty()) {
                tvConfigDescription.setText(description);
                tvConfigDescription.setVisibility(View.VISIBLE);
            } else {
                tvConfigDescription.setText(R.string.no_description);
                tvConfigDescription.setVisibility(View.GONE);
            }

            // Show component count
            Map<String, Component> componentsMap = configuration.getComponents();
            if (componentsMap != null) {
                tvComponentCount.setText(context.getString(R.string.component_count, componentsMap.size()));
            } else {
                tvComponentCount.setText(context.getString(R.string.component_count, 0));
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ConfigurationDetailActivity.class);
            intent.putExtra("configurationId", configuration.getId());
            context.startActivity(intent);
        }
    }
} 