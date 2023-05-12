package com.example.medca.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medca.databinding.ActivityMedicineItemBinding;
import com.example.medca.listeners.UserListener;
import com.example.medca.models.User;

import java.util.List;

public class medAdapter extends RecyclerView.Adapter<medAdapter.UserViewHolder> {
    private final List<User> users;
    private final UserListener userListener;


    public medAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public medAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityMedicineItemBinding activityMedicineItemBinding = ActivityMedicineItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent,
                false
        );

        return new medAdapter.UserViewHolder(activityMedicineItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull medAdapter.UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        ActivityMedicineItemBinding binding;

        UserViewHolder(ActivityMedicineItemBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;

        }

        void setUserData (User user){
            binding.txtMedName.setText(user.medicine);
           }

    }


}
