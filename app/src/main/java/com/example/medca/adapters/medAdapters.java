package com.example.medca.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

public interface medAdapters {
    @NonNull
    medAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    void onBindViewHolder(@NonNull UsersAdapter.UserViewHolder holder, int position);

    void onBindViewHolder(@NonNull medAdapter.UserViewHolder holder, int position);

    int getItemCount();
}
