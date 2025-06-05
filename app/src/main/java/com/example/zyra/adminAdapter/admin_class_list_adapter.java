package com.example.zyra.adminAdapter;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zyra.R;
import com.example.zyra.modelData.subject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class admin_class_list_adapter extends RecyclerView.Adapter<admin_class_list_adapter.viewHolder> {

    Context context;
    List<subject> items;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(subject item);
        void onItemEditClicked(subject item);
        void onItemDeletedClicked(subject item);
    }

    public admin_class_list_adapter(Context context, List<subject> items, OnItemClickListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public admin_class_list_adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.admin_class_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull admin_class_list_adapter.viewHolder holder, int position) {
        holder.className.setText(items.get(position).getClassName());
        holder.classCode.setText(items.get(position).getClassCode());
        holder.lecturerName.setText(items.get(position).getLecturerName());

        if (position % 3 == 0) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#fcf0ff"));
            holder.editButton.setBackgroundColor(Color.parseColor("#fcf0ff"));
            holder.deleteButton.setBackgroundColor(Color.parseColor("#fcf0ff"));
        } else if (position % 3 == 1) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ebfffb"));
            holder.editButton.setBackgroundColor(Color.parseColor("#ebfffb"));
            holder.deleteButton.setBackgroundColor(Color.parseColor("#ebfffb"));
        } else if (position % 3 == 2) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#F8F7C7"));
            holder.editButton.setBackgroundColor(Color.parseColor("#F8F7C7"));
            holder.deleteButton.setBackgroundColor(Color.parseColor("#F8F7C7"));
        }

        holder.editButton.setOnClickListener(view -> {
            listener.onItemEditClicked(items.get(position));
        });

        holder.deleteButton.setOnClickListener(view -> {
            listener.onItemDeletedClicked(items.get(position));
        });

        holder.itemView.setOnClickListener(view -> {
           listener.onItemClick(items.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView className, classCode, lecturerName;
        ImageView lecturerPhoto;
        CardView cardView;
        Button editButton, deleteButton;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.adminClassListClassName);
            classCode = itemView.findViewById(R.id.adminClassListClassCode);
            lecturerName = itemView.findViewById(R.id.adminClassListLecturerName);
            lecturerPhoto = itemView.findViewById(R.id.adminClassListLecturerProfile);
            cardView = itemView.findViewById(R.id.adminClassListCardView);
            editButton = itemView.findViewById(R.id.adminClassEditButton);
            deleteButton = itemView.findViewById(R.id.adminClassDeleteButton);
        }
    }
}
