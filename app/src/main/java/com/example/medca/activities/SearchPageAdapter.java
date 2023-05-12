package com.example.medca.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medca.R;
import com.example.medca.databinding.ActivityMedicineItemBinding;
import com.example.medca.listeners.MedicineListener;
import com.example.medca.models.User;

import java.util.ArrayList;

public class SearchPageAdapter extends RecyclerView.Adapter<SearchPageAdapter.ViewHolder>{

private ArrayList<MedicineModal> medicineModalArrayList;
private Context context;

public SearchPageAdapter(ArrayList<MedicineModal> medicineModalArrayList, Context context){
    this.medicineModalArrayList = medicineModalArrayList;
    this.context = context;
}

public void filterList(ArrayList<MedicineModal> filterList){

    medicineModalArrayList = filterList;

    notifyDataSetChanged();
}

    @NonNull
    @Override
    public SearchPageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_medicine_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        MedicineModal modal = medicineModalArrayList.get(position);
        holder.medName.setText(modal.getGenName());
        holder.genName.setText(modal.getMedicineName());
    }


    @Override
    public int getItemCount() {
        // returning the size of array list.
        return medicineModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our views.
        private TextView medName;
        private TextView genName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            medName = itemView.findViewById(R.id.txtMedName);
            genName = itemView.findViewById(R.id.txtBrand);
        }
    }
}



