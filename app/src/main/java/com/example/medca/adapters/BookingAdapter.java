package com.example.medca.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medca.R;
import com.example.medca.databinding.ItemContainerLayoutBinding;

import com.example.medca.models.booked;
import com.example.medca.utilities.Constants;
import com.example.medca.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.CounsellingViewHolder> {

    private final List<booked> bookeds;

    public BookingAdapter(List<booked> bookeds) {
        this.bookeds = bookeds;
    }

    private Context context;
    private PreferenceManager preferenceManager;

    @NonNull
    @Override
    public CounsellingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerLayoutBinding itemContainerLayoutBinding = ItemContainerLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        context = parent.getContext();
        preferenceManager = new PreferenceManager(context);
        return new CounsellingViewHolder(itemContainerLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CounsellingViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //holder.setCounsellingData(bookeds.get(position));
        holder.binding.bookedDate.setText(bookeds.get(position).bookdate);
        holder.binding.bookedTime.setText(bookeds.get(position).booktime);
        holder.binding.bookedReason.setText(bookeds.get(position).bookreason);
        holder.binding.statusCounselling.setText(bookeds.get(position).bookstatus);
        if(bookeds.get(position).bookstatus.equals("Approved")) {
            holder.binding.statusSign.setBackgroundColor(Color.parseColor("#008000"));
            holder.binding.statusSign.setVisibility(View.VISIBLE);
            holder.binding.cancelSchedule.setVisibility(View.GONE);
        } else if(bookeds.get(position).bookstatus.equals("Waiting for Completion")) {
            holder.binding.statusSign.setBackgroundColor(Color.parseColor("#008000"));
            holder.binding.statusSign.setVisibility(View.VISIBLE);
            holder.binding.cancelSchedule.setVisibility(View.GONE);
        }else if(bookeds.get(position).bookstatus.equals("Suggested")) {
            holder.binding.statusSign.setBackgroundColor(Color.parseColor("#808080"));
            holder.binding.statusSign.setVisibility(View.VISIBLE);
            holder.binding.cancelSchedule.setVisibility(View.GONE);
        }else if(bookeds.get(position).bookstatus.equals("Cancelled")) {
            holder.binding.statusSign.setBackgroundColor(Color.parseColor("#ff0000"));
            holder.binding.statusSign.setVisibility(View.VISIBLE);
        }else {
            holder.binding.statusSign.setBackgroundColor(Color.parseColor("#808080"));
            holder.binding.statusSign.setVisibility(View.VISIBLE);
        }
        holder.binding.cancelSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                        .setTitle("Cancel Schedule")
                        .setMessage("Are you sure want to Cancel?")
                        .setIcon(R.drawable.ic_cancel)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseFirestore database = FirebaseFirestore.getInstance();

                                database.collection(Constants.KEY_COLLECTION_BOOKINGS)
                                        .document(preferenceManager.getString(Constants.KEY_BOOKING_ID))
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        bookeds.remove(position);
                                        notifyItemRemoved(position);
                                        Toast.makeText(context, "Schedule is removed", Toast.LENGTH_SHORT).show();
                                        HashMap<String, Object> cancelCounselling = new HashMap<>();
                                        cancelCounselling.put(Constants.KEY_BOOKINGS_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                                        cancelCounselling.put(Constants.KEY_BOOKINGS_TypeOfBooking, Constants.KEY_BOOKINGS_TypeOfBooking);
                                        cancelCounselling.put(Constants.KEY_BOOKINGS_LNAME, preferenceManager.getString(Constants.KEY_BOOKINGS_LNAME));
                                        cancelCounselling.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
                                        cancelCounselling.put(Constants.KEY_MIDDLE_NAME, preferenceManager.getString(Constants.KEY_MIDDLE_NAME));
                                        cancelCounselling.put(Constants.KEY_BOOKINGS_ADDRESS, preferenceManager.getString(Constants.KEY_BOOKINGS_ADDRESS));
                                        cancelCounselling.put(Constants.KEY_EMAIL, preferenceManager.getString(Constants.KEY_EMAIL));
                                        cancelCounselling.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));
                                        cancelCounselling.put(Constants.KEY_BOOKINGS_STATUS, "Cancelled");
                                        cancelCounselling.put("notificationType ", "Bookings");
                                        cancelCounselling.put("read", false);
                                        cancelCounselling.put(Constants.KEY_CREATED_AT, new Date());
                                        database.collection(Constants.KEY_COLLECTION_CANCEL_BOOKINGS).add(cancelCounselling);
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookeds.size();
    }

    class CounsellingViewHolder extends RecyclerView.ViewHolder {

        ItemContainerLayoutBinding binding;

        CounsellingViewHolder(ItemContainerLayoutBinding itemCounsellingBinding) {
            super(itemCounsellingBinding.getRoot());
            binding = itemCounsellingBinding;
        }

        void setCounsellingData(booked booked) {
            binding.bookedDate.setText(booked.bookdate);
            binding.bookedTime.setText(booked.booktime);
            binding.bookedReason.setText(booked.bookreason);
            binding.statusCounselling.setText(booked.bookstatus);
            if(booked.bookstatus.equals("Approved")) {
                binding.statusSign.setBackgroundColor(Color.parseColor("#008000"));
                binding.statusSign.setVisibility(View.VISIBLE);
            } else if(booked.bookstatus.equals("Cancelled")) {
                binding.statusSign.setBackgroundColor(Color.parseColor("#ff0000"));
                binding.statusSign.setVisibility(View.VISIBLE);
            }else {
                binding.statusSign.setBackgroundColor(Color.parseColor("#808080"));
                binding.statusSign.setVisibility(View.VISIBLE);
            }
        }
    }

}