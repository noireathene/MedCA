package com.example.medca.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medca.R;
import com.example.medca.models.timeslot;
import com.example.medca.utilities.Constants;

import java.util.List;

public class timeSlotAdapter extends RecyclerView.Adapter<timeSlotAdapter.MyViewHolder> {

    Context context;
    List<timeslot> timeslotList;

    public timeSlotAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.timeslot_cardview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.txtTimeSlot.setText(new StringBuilder(Constants.convertTimeslotToString(position)).toString());
            if(timeslotList.size() == 0) {
                holder.txtTimeSlotDescription.setText("Available");
                holder.txtTimeSlotDescription.setTextColor(context.getResources().getColor(R.color.black));
                holder.txtTimeSlot.setTextColor(context.getResources().getColor(R.color.black));
                holder.cardTimeSlot.setCardBackgroundColor(context.getResources().getColor(R.color.white));

            }else {
                for (timeslot timeslot1: timeslotList){
                    int slot = Integer.parseInt(timeslot1.getSlot().toString());
                    if (slot == 1){
                        holder.cardTimeSlot.setCardBackgroundColor(context.getResources().getColor(R.color.mdtp_dark_gray));
                        holder.txtTimeSlotDescription.setText("Full");
                        holder.txtTimeSlotDescription.setTextColor(context.getResources().getColor(R.color.white));
                        holder.txtTimeSlot.setTextColor(context.getResources().getColor(R.color.white));
                    }

                }
            }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTimeSlot, txtTimeSlotDescription;
        CardView cardTimeSlot;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardTimeSlot = (CardView) itemView.findViewById(R.id.card_time_slot);
            txtTimeSlot = (TextView) itemView.findViewById(R.id.txtTimeslot);
            txtTimeSlotDescription = (TextView) itemView.findViewById(R.id.txtTimeslotDescription);


        }
    }
}
