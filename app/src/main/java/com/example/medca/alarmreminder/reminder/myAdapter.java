package com.example.medca.alarmreminder.reminder;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medca.PrescriptionList;
import com.example.medca.R;
import com.example.medca.alarmreminder.data.Model;

import java.util.ArrayList;


    public class myAdapter extends RecyclerView.Adapter<myAdapter.myviewholder> {

        private ArrayList<com.example.medca.alarmreminder.data.Model> dataholder = new ArrayList<Model>();
        private OnMedListener mOnMedListener;

    public myAdapter(ArrayList<com.example.medca.alarmreminder.data.Model> dataholder, OnMedListener onMedListener) {
        this.dataholder = dataholder;
        this.mOnMedListener = onMedListener;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_alarm_items, parent, false);
    return new myviewholder(view, mOnMedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
    holder.mTitle.setText(dataholder.get(position).getTitle());
    holder.mDate.setText(dataholder.get(position).getDate());
    holder.mTime.setText(dataholder.get(position).getTime());

        Log.d("TEST_TAG", "Gikan sa adapter: "+dataholder.get(position).getTitle());
        Log.d("TEST_TAG", "Gikan sa adapter: "+dataholder.get(position).getDate());
        Log.d("TEST_TAG", "Gikan sa adapter: "+dataholder.get(position).getTime());
    }

    @Override
    public int getItemCount() {
    return dataholder.size(); }

    public class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView mTitle,mDate,mTime;
    OnMedListener onMedListener;


    public myviewholder(@NonNull View itemView, OnMedListener onMedListener) {
    super (itemView);

    mTitle = (TextView) itemView.findViewById(R.id.txtTitle);
    mDate = (TextView) itemView.findViewById(R.id.txtDate);
    mTime = (TextView) itemView.findViewById(R.id.txtTime);
    this.onMedListener = onMedListener;

    itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onMedListener.onMedClick(getAdapterPosition());
        }
        }
        public interface OnMedListener{
        void onMedClick(int position);
        }
    }
