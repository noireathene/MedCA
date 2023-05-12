package com.example.medca.activities;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.medca.databinding.ActivityPatientviewItemBinding;
import com.example.medca.listeners.UserListener;
import com.example.medca.models.User;

import java.util.List;

public class myPatientViewAdapter extends RecyclerView.Adapter<myPatientViewAdapter.myviewholder> {

    private final List<User> users;
    private final UserListener userListener;

    public myPatientViewAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public myPatientViewAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityPatientviewItemBinding activityPatientviewItemBinding = ActivityPatientviewItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent,
                false);
         return new myPatientViewAdapter.myviewholder(activityPatientviewItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull myPatientViewAdapter.myviewholder holder, int position) {
        holder.setUserData(users.get(position));

        Log.d("TEST_TAG", "Gikan sa adapter: "+users.get(position).name);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    class myviewholder extends RecyclerView.ViewHolder{

        ActivityPatientviewItemBinding binding;

        myviewholder(ActivityPatientviewItemBinding activityPatientviewItemBinding){
            super(activityPatientviewItemBinding.getRoot());
            binding = activityPatientviewItemBinding;

        }

        void setUserData (User user){
            binding.txtName.setText(user.name);
            binding.bookedDate.setText(user.schedule);
            binding.bookedTime.setText(user.time);
            binding.bookedReason.setText(user.reason);
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));

            Log.d("TEST_1", user.name);
            Log.d("TEST_1", user.schedule);
            Log.d("TEST_1", user.time);
        }

    }
}