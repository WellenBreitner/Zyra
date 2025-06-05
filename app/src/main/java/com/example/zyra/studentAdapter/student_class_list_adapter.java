package com.example.zyra.studentAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zyra.modelData.subject;
import com.example.zyra.R;

import java.util.List;

public class student_class_list_adapter extends RecyclerView.Adapter<student_class_list_adapter.viewHolder> {

    Context context;
    List<subject> items;
    OnItemClickListener listener;

    public interface OnItemClickListener{
        void onClick(subject item);
    }


    public student_class_list_adapter(Context context, List<subject> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public student_class_list_adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.student_class_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull student_class_list_adapter.viewHolder holder, int position) {
        holder.className.setText(items.get(position).getClassName());
        holder.classCode.setText(items.get(position).getClassCode());
        holder.lecturerName.setText(items.get(position).getLecturerName());

        if (position % 3 == 0) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#fcf0ff"));
        } else if (position % 3 == 1) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ebfffb"));
        } else if (position % 3 == 2) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#F8F7C7"));
        }

        holder.itemView.setOnClickListener(view -> {
            listener.onClick(items.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView className, classCode, lecturerName;
        CardView cardView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.studentClassListClassName);
            classCode = itemView.findViewById(R.id.studentClassListClassCode);
            lecturerName = itemView.findViewById(R.id.studentClassListLecturerName);
            cardView = itemView.findViewById(R.id.classListCardView);
        }
    }
}
