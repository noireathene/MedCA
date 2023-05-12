package com.example.medca.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medca.databinding.ItemContainerUserBinding;
import com.example.medca.listeners.UserListener;
import com.example.medca.models.User;

import java.util.List;

public class patientUsersAdapter extends RecyclerView.Adapter<patientUsersAdapter.UserViewHolder> {
    private final List<User> list;
    private final UserListener userListener;

    public patientUsersAdapter(List<User> list, UserListener userListener) {
        this.list = list;
        this.userListener = userListener;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent,
                false);
                return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull patientUsersAdapter.UserViewHolder holder, int position) {
        holder.setUserData(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        ItemContainerUserBinding binding;

        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;

        }

        void setUserData (User user){
            binding.name.setText(user.name);
            binding.email.setText(user.email);
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }

    }
}
