package com.example.medca.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medca.databinding.MonitoringItemsBinding;
import com.example.medca.listeners.UserListener;
import com.example.medca.models.User;

import java.util.List;

public class medicationAdapter extends RecyclerView.Adapter<medicationAdapter.MedicationViewHolder> {

    private final List<User> users;
    private final UserListener userListener;

    public medicationAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public medicationAdapter.MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MonitoringItemsBinding monitoringItemsBinding = MonitoringItemsBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new medicationAdapter.MedicationViewHolder(monitoringItemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull medicationAdapter.MedicationViewHolder holder, int position) {
        holder.setUserData(users.get(position));

    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    class MedicationViewHolder extends RecyclerView.ViewHolder{

        MonitoringItemsBinding binding;

        MedicationViewHolder(MonitoringItemsBinding monitoringItemsBinding){
            super(monitoringItemsBinding.getRoot());
            binding = monitoringItemsBinding;
        }

        void setUserData (User users){
            binding.txtTitle.setText(users.medicine);
            binding.txtDate.setText(users.schedule);
            binding.txtTime.setText(users.time);
            binding.docName.setText(users.name);
            binding.status.setText(users.status);
            binding.diagnosis.setText(users.diagnosis);
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(users));
        }
    }
}
