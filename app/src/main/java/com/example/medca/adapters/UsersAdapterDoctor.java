package com.example.medca.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medca.activities.SearchDoctor;
import com.example.medca.databinding.ItemContainerUserBinding;
import com.example.medca.listeners.UserListenerDoctors;
import com.example.medca.models.User;

import java.util.List;

public class UsersAdapterDoctor extends RecyclerView.Adapter<UsersAdapterDoctor.UserViewHolder> {

    private final List<User> users;
    private final UserListenerDoctors userListener;

    public UsersAdapterDoctor(List<User> users, SearchDoctor userListener) {
        this.users = users;
        this.userListener = userListener;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent,
                false
        );

        return new UsersAdapterDoctor.UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
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
