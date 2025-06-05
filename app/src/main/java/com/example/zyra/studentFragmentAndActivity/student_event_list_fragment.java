package com.example.zyra.studentFragmentAndActivity;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zyra.R;
import com.example.zyra.ROOM.Database;
import com.example.zyra.ROOM.EventDao;
import com.example.zyra.ROOM.EventDatabase;
import com.example.zyra.ROOM.EventRoom;
import com.example.zyra.studentAdapter.student_class_event_list_adapter;
import com.example.zyra.modelData.event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class student_event_list_fragment extends Fragment implements student_class_event_list_adapter.OnClickItemListener {

    List<event> items = new ArrayList<>();
    RecyclerView recyclerView;
    View v;
    TextView text0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(container != null){
            container.removeAllViews();
        }

       v =  inflater.inflate(R.layout.fragment_student_class_assignment_list, container, false);

        initializeUI();
        initializeListener();


        return v;
    }

    private void initializeListener() {
    }

    private void initializeUI() {
        recyclerView = v.findViewById(R.id.RVForEventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new student_class_event_list_adapter(getActivity(),items,this));
        text0 = v.findViewById(R.id.text0);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("subject_code_student", Context.MODE_PRIVATE);
        String subjectCode = sharedPreferences.getString("subject_code","");

        getTheEventList(subjectCode);
    }

    private void getTheEventList(String subjectCode) {
        items.clear();
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("events");
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()){
                        event eventData = eventSnapshot.getValue(event.class);
                        String eventCode = eventData.getEventClassCode();
                        if (eventCode !=null && eventCode.equals(subjectCode)){
                            items.add(eventData);
                        }
                    }if (items.isEmpty()) {
                        text0.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        text0.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                }else{
                    Toast.makeText(getContext(), "Event not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: error.getMessage()", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(event item) {
        Intent intent = new Intent(getContext(), student_event_details_activity.class);
        intent.putExtra("event_detail",item);
        startActivity(intent);
    }

    @Override
    public void onClickDownloadItem(event item) {
        downloadEventToLocalDatabase(item);
    }

    private void downloadEventToLocalDatabase(event eventData) {
        new Thread(() -> {
            EventRoom eventEntity = new EventRoom(
                    eventData.getEventId(),
                    eventData.getEventName(),
                    eventData.getEventType(),
                    eventData.getEventClassCode(),
                    eventData.getEventDeadlineDate(),
                    eventData.getEventDeadlineTime(),
                    eventData.getEventDesc(),
                    eventData.getEventSubmissionLink(),
                    eventData.getEventImage()
            );

            Database.getInstance(requireContext())
                    .getEventDatabase()
                            .eventDao().insertEvent(eventEntity);

            getActivity().runOnUiThread(() ->
                    Toast.makeText(requireContext(), "Event downloaded", Toast.LENGTH_SHORT).show());
        }).start();
    }


}