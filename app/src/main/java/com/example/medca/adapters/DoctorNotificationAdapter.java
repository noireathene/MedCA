package com.example.medca.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medca.databinding.RecyclerAdapterDoctorNotificationBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import FCM.NotificationModel;

public class DoctorNotificationAdapter extends RecyclerView.Adapter<DoctorNotificationAdapter.MyViewHolder> {

    private ArrayList<NotificationModel> arrListNotification;
    private Context context;
    private Activity activity;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public DoctorNotificationAdapter(ArrayList<NotificationModel> arrListNotification, Context context, Activity activity) {
        this.arrListNotification = arrListNotification;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerAdapterDoctorNotificationBinding recyclerAdapterDoctorNotificationBinding = RecyclerAdapterDoctorNotificationBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent,
                false
        );
        return new MyViewHolder(recyclerAdapterDoctorNotificationBinding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setData(arrListNotification.get(position));
    }

    @Override
    public int getItemCount() {
        return arrListNotification.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        RecyclerAdapterDoctorNotificationBinding binding;
        public MyViewHolder(RecyclerAdapterDoctorNotificationBinding recyclerAdapterDoctorNotificationBinding, OnItemClickListener listener) {
            super(recyclerAdapterDoctorNotificationBinding.getRoot());
            binding = recyclerAdapterDoctorNotificationBinding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
        void setData(NotificationModel notif){
            binding.textBody.setText(notif.getBody());
            binding.textTitle.setText(notif.getTitle());

            Long timeStamp = notif.getDate().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a dd,MMM,''yy");
            String ConvertedDate = simpleDateFormat.format(new Date(timeStamp));

            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            String time = timeFormat.format(new Date(timeStamp));

            if(DateUtils.isToday(timeStamp)){
                binding.textDate.setText(time);
            }else{
                binding.textDate.setText(ConvertedDate);
            }
        }
    }
}
