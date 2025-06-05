package com.example.zyra.studentAdapter;

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

public class student_class_event_list_adapter extends RecyclerView.Adapter<student_class_event_list_adapter.viewHolder> {

    Context context;
    List<event> items;
    OnClickItemListener listener;

    public interface OnClickItemListener{
        void onClick(event item);
        void onClickDownloadItem(event item);
    }

    public student_class_event_list_adapter(Context context, List<event> items, OnClickItemListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public student_class_event_list_adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.student_event_list, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull student_class_event_list_adapter.viewHolder holder, int position) {
        holder.studentEventListName.setText(String.format("%s %s", items.get(position).getEventName(), items.get(position).getEventType()));
        holder.studentEventListDeadline.setText(String.format("%s %s", items.get(position).getEventDeadlineDate(), items.get(position).getEventDeadlineTime())) ;
        holder.studentEventListClass.setText(items.get(position).getEventClassCode());
        holder.studentEventListLogoImage.setImageResource(items.get(position).getEventImage());

        if (position % 3 == 0) {
            holder.studentEventListCardView.setCardBackgroundColor(Color.parseColor("#fcf0ff"));
        } else if (position % 3 == 1) {
            holder.studentEventListCardView.setCardBackgroundColor(Color.parseColor("#ebfffb"));
        } else if (position % 3 == 2) {
            holder.studentEventListCardView.setCardBackgroundColor(Color.parseColor("#F8F7C7"));
        }

        holder.itemView.setOnClickListener(view -> {
            listener.onClick(items.get(position));
        });

        holder.studentEventDownloadButton.setOnClickListener(view->{
            listener.onClickDownloadItem(items.get(position));
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView studentEventListName, studentEventListDeadline, studentEventListClass;
        MaterialButton studentEventDownloadButton;
        ImageView studentEventListLogoImage;
        CardView studentEventListCardView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            studentEventListLogoImage = itemView.findViewById(R.id.studentEventListLogoImage);
            studentEventListName = itemView.findViewById(R.id.studentEventListName);
            studentEventListDeadline = itemView.findViewById(R.id.studentEventListDeadline);
            studentEventListClass = itemView.findViewById(R.id.studentEventListClass);
            studentEventDownloadButton = itemView.findViewById(R.id.studentEventDownloadButton);
            studentEventListCardView = itemView.findViewById(R.id.studentEventListCardView);
        }
    }
}
