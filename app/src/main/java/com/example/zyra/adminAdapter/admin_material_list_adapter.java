package com.example.zyra.adminAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zyra.R;
import com.example.zyra.modelData.material;
import java.util.List;

public class admin_material_list_adapter extends RecyclerView.Adapter<admin_material_list_adapter.viewHolder> {

    Context context;
    List<material> items;
    OnClickItemListener listener;

    public interface OnClickItemListener{
        void onItemClicked(material item);
        void onItemEditClicked(material item);
        void onItemDeleteClicked(material item);

    }

    public admin_material_list_adapter(Context context, List<material> items, OnClickItemListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public admin_material_list_adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.admin_material_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull admin_material_list_adapter.viewHolder holder, int position) {
        holder.adminMaterialListName.setText(items.get(position).getMaterialName());
        holder.adminMaterialListPostDate.setText(items.get(position).getMaterialPostDate());

        if (position % 3 == 0) {
            holder.adminMaterialListCardView.setCardBackgroundColor(Color.parseColor("#fcf0ff"));
            holder.adminEditMaterial.setBackgroundColor(Color.parseColor("#fcf0ff"));
            holder.adminDeleteMaterial.setBackgroundColor(Color.parseColor("#fcf0ff"));
        } else if (position % 3 == 1) {
            holder.adminMaterialListCardView.setCardBackgroundColor(Color.parseColor("#ebfffb"));
            holder.adminEditMaterial.setBackgroundColor(Color.parseColor("#ebfffb"));
            holder.adminDeleteMaterial.setBackgroundColor(Color.parseColor("#ebfffb"));
        } else if (position % 3 == 2) {
            holder.adminMaterialListCardView.setCardBackgroundColor(Color.parseColor("#F8F7C7"));
            holder.adminEditMaterial.setBackgroundColor(Color.parseColor("#F8F7C7"));
            holder.adminDeleteMaterial.setBackgroundColor(Color.parseColor("#F8F7C7"));
        }

        holder.itemView.setOnClickListener(view -> {
            listener.onItemClicked(items.get(position));
        });

        holder.adminDeleteMaterial.setOnClickListener(view -> {
            listener.onItemDeleteClicked(items.get(position));
        });

        holder.adminEditMaterial.setOnClickListener(view -> {
            listener.onItemEditClicked(items.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        CardView adminMaterialListCardView;
        Button adminEditMaterial,adminDeleteMaterial;
        TextView adminMaterialListName,adminMaterialListPostDate;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            adminMaterialListCardView = itemView.findViewById(R.id.adminMaterialListCardView);
            adminMaterialListName = itemView.findViewById(R.id.adminMaterialListName);
            adminMaterialListPostDate = itemView.findViewById(R.id.adminMaterialListPostDate);
            adminEditMaterial = itemView.findViewById(R.id.adminMaterialEditButton);
            adminDeleteMaterial = itemView.findViewById(R.id.adminMaterialDeleteButton);
        }
    }
}
