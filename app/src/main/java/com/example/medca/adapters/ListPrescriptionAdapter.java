package com.example.medca.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medca.databinding.PrescriptionItemBinding;
import com.example.medca.listeners.UserListener;
import com.example.medca.models.User;

import java.util.List;

public class ListPrescriptionAdapter extends RecyclerView.Adapter<ListPrescriptionAdapter.myviewholder> {

    private final List<User> users;
    private final UserListener userListener;

    public ListPrescriptionAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }


    @NonNull
    @Override
    public ListPrescriptionAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PrescriptionItemBinding prescriptionItemBinding = PrescriptionItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ListPrescriptionAdapter.myviewholder(prescriptionItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPrescriptionAdapter.myviewholder holder, int position) {
        holder.setUserData(users.get(position));

    }


    @Override
    public int getItemCount() { return users.size(); }
    class myviewholder extends RecyclerView.ViewHolder{

        PrescriptionItemBinding binding;

        myviewholder(PrescriptionItemBinding prescriptionItemBinding){
            super(prescriptionItemBinding.getRoot());
            binding = prescriptionItemBinding;
        }

        void setUserData (User users){
            binding.diagnosed.setText(users.diagnosis);
            binding.MedicineName.setText(users.medicine);
            binding.MedIntake.setText(users.intake);
            binding.schedule.setText(users.schedule);
            binding.docName.setText(users.name);
            binding.status.setText(users.status);
            binding.diagnosed.setText(users.diagnosis);
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(users));
        }
    }
}
