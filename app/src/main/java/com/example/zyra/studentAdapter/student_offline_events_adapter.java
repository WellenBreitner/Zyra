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

import com.example.zyra.ROOM.EventRoom;
import com.example.zyra.R;

import java.util.List;

import io.reactivex.internal.util.AppendOnlyLinkedArrayList;

public class student_offline_events_adapter extends RecyclerView.Adapter<student_offline_events_adapter.viewHolder> {

    Context context;
    List<EventRoom> items;
    OnclickItemDownload listener;

    public interface OnclickItemDownload{
        void itemClicked(EventRoom item);
    }

    public student_offline_events_adapter(Context context, List<EventRoom> items,OnclickItemDownload listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public student_offline_events_adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new student_offline_events_adapter.viewHolder(LayoutInflater.from(context).inflate(R.layout.student_downloaded_event,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull student_offline_events_adapter.viewHolder holder, int position) {
         holder.offlineEventName.setText(String.format("%s %s", items.get(position).getEventName(), items.get(position).getEventType()));
         holder.offlineEventClassCode.setText(items.get(position).getEventClassCode());
         holder.offlineEventListDeadline.setText(String.format("%s %s", items.get(position).getEventDeadlineDate(), items.get(position).getEventDeadlineTime()));
         holder.offlineEventListLogoImage.setImageResource(items.get(position).getEventImage());

         holder.offlineNotificationCardView.setCardBackgroundColor(Color.parseColor("#ffffffff"));

         if (position % 3 == 0) {
             holder.offlineNotificationCardView.setCardBackgroundColor(Color.parseColor("#fcf0ff"));
         } else if (position % 3 == 1) {
             holder.offlineNotificationCardView.setCardBackgroundColor(Color.parseColor("#ebfffb"));
         } else if (position % 3 == 2) {
             holder.offlineNotificationCardView.setCardBackgroundColor(Color.parseColor("#F8F7C7"));
         }

         holder.itemView.setOnClickListener(view -> {
             listener.itemClicked(items.get(position));
         });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView offlineEventName,offlineEventClassCode,offlineEventListDeadline;
        ImageView offlineEventListLogoImage;
        CardView offlineNotificationCardView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            offlineEventName = itemView.findViewById(R.id.offlineEventListName);
            offlineEventClassCode = itemView.findViewById(R.id.offlineEventListClass);
            offlineEventListDeadline = itemView.findViewById(R.id.offlineEventListDeadline);
            offlineEventListLogoImage = itemView.findViewById(R.id.offlineEventListLogoImage);
            offlineNotificationCardView = itemView.findViewById(R.id.offlineEventListCardView);
        }
    }
}
