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

import com.example.zyra.R;
import com.example.zyra.modelData.material;
import java.util.List;

public class student_material_list_adapter extends RecyclerView.Adapter<student_material_list_adapter.viewHolder> {

    Context context;
    List<material> items;
    OnItemClickListener listener;

    public interface OnItemClickListener{
        void onClick(material items);
    }

    public student_material_list_adapter(Context context, List<material> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }


    @NonNull
    @Override
    public student_material_list_adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new student_material_list_adapter.viewHolder(LayoutInflater.from(context).inflate(R.layout.student_material_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull student_material_list_adapter.viewHolder holder, int position) {
        holder.studentMaterialListName.setText(items.get(position).getMaterialName());
        holder.studentMaterialListPostDate.setText(items.get(position).getMaterialPostDate());

        if (position % 3 == 0) {
            holder.studentMaterialListCardView.setCardBackgroundColor(Color.parseColor("#fcf0ff"));
        } else if (position % 3 == 1) {
            holder.studentMaterialListCardView.setCardBackgroundColor(Color.parseColor("#ebfffb"));
        } else if (position % 3 == 2) {
            holder.studentMaterialListCardView.setCardBackgroundColor(Color.parseColor("#F8F7C7"));
        }

        holder.itemView.setOnClickListener(View -> {
            listener.onClick(items.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class viewHolder extends RecyclerView.ViewHolder {
        CardView studentMaterialListCardView;
        TextView studentMaterialListName,studentMaterialListPostDate;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            studentMaterialListCardView = itemView.findViewById(R.id.studentMaterialListCardView);
            studentMaterialListName = itemView.findViewById(R.id.studentMaterialListName);
            studentMaterialListPostDate = itemView.findViewById(R.id.studentMaterialListPostDate);
        }
    }
}
