package com.example.zyra.adminAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zyra.R;
import com.example.zyra.modelData.event;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class admin_event_list_adapter extends RecyclerView.Adapter<admin_event_list_adapter.viewHolder> {

    Context context;
    List<event> items;
    OnClickItemListener listener;

    public interface OnClickItemListener{
        void onItemClicked(event item);
        void onItemEditClicked(event item);
        void onItemDeletedClicked(event item);
    }

    public admin_event_list_adapter(Context context, List<event> items, OnClickItemListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public admin_event_list_adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.admin_event_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull admin_event_list_adapter.viewHolder holder, int position) {
        holder.adminEventListName.setText(String.format("%s %s", items.get(position).getEventName(), items.get(position).getEventType()));
        holder.adminEventListClass.setText(items.get(position).getEventClassCode());
        holder.adminEventListDeadline.setText(String.format("%s %s", items.get(position).getEventDeadlineDate(), items.get(position).getEventDeadlineTime()));
        holder.adminEventListLogoImage.setImageResource(items.get(position).getEventImage());
        if (position % 3 == 0) {
            holder.adminEventListCardView.setCardBackgroundColor(Color.parseColor("#fcf0ff"));
            holder.adminEventEditButton.setBackgroundColor(Color.parseColor("#fcf0ff"));
            holder.adminEventDeleteButton.setBackgroundColor(Color.parseColor("#fcf0ff"));
        } else if (position % 3 == 1) {
            holder.adminEventListCardView.setCardBackgroundColor(Color.parseColor("#ebfffb"));
            holder.adminEventEditButton.setBackgroundColor(Color.parseColor("#ebfffb"));
            holder.adminEventDeleteButton.setBackgroundColor(Color.parseColor("#ebfffb"));
        } else if (position % 3 == 2) {
            holder.adminEventListCardView.setCardBackgroundColor(Color.parseColor("#F8F7C7"));
            holder.adminEventEditButton.setBackgroundColor(Color.parseColor("#F8F7C7"));
            holder.adminEventDeleteButton.setBackgroundColor(Color.parseColor("#F8F7C7"));
        }

        holder.itemView.setOnClickListener(view -> {
            listener.onItemClicked(items.get(position));
        });


        holder.adminEventDeleteButton.setOnClickListener(view -> {
            listener.onItemDeletedClicked(items.get(position));
        });

        holder.adminEventEditButton.setOnClickListener(view -> {
            listener.onItemEditClicked(items.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView adminEventListName,adminEventListClass,adminEventListDeadline;
        CardView adminEventListCardView;
        MaterialButton adminEventEditButton,adminEventDeleteButton;
        ImageView adminEventListLogoImage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            adminEventListCardView = itemView.findViewById(R.id.adminEventListCardView);
            adminEventListName = itemView.findViewById(R.id.adminEventListName);
            adminEventListClass = itemView.findViewById(R.id.adminEventListClass);
            adminEventListDeadline = itemView.findViewById(R.id.adminEventListDeadline);
            adminEventEditButton = itemView.findViewById(R.id.adminEventEditButton);
            adminEventDeleteButton = itemView.findViewById(R.id.adminEventDeleteButton);
            adminEventListLogoImage = itemView.findViewById(R.id.adminEventListLogoImage);
        }
    }
}
