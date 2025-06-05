package com.example.zyra.studentFragmentAndActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.zyra.R;
import com.example.zyra.ROOM.Database;
import com.example.zyra.ROOM.EventRoom;
import com.example.zyra.studentAdapter.student_class_event_list_adapter;
import com.example.zyra.modelData.event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class student_schedule_menu_fragment extends Fragment implements student_class_event_list_adapter.OnClickItemListener {
    View v;
    List<event> items = new ArrayList<>();
    CalendarView calender;
    RecyclerView recyclerView;
    TextView text0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(container != null){
            container.removeAllViews();
        }

        v = inflater.inflate(R.layout.fragment_student_schedule_menu, container, false);


        initializeUI();
        initializeListener();
        return v;
    }

    private void initializeListener() {
        calender.setOnDateChangeListener((view,year,month,dayOfMonth)->{
            String selectedDate = String.format("%02d-%02d-%d", dayOfMonth, (month + 1), year);

            items.clear();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String userID;
            if (currentUser == null){
                userID = "";
                Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
            }else{
                userID = currentUser.getUid();
            }

            DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("events");
            DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("registeredSubjects");
            List<String> eventClassCodeList = new ArrayList<>();

            studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot eventSnapshot : snapshot.getChildren()){
                            String eventClassCodeData = eventSnapshot.getValue(String.class);
                            if (eventClassCodeData != null){
                                eventClassCodeList.add(eventClassCodeData);
                            }
                        }
                    }else {
                        Toast.makeText(getContext(), "Event not found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot eventSnapshot : snapshot.getChildren()){
                            event eventData = eventSnapshot.getValue(event.class);
                            String eventCode = eventData.getEventClassCode();
                            String eventDeadlineDate = eventData.getEventDeadlineDate();
                            if ((eventData != null) && (selectedDate.equals(eventDeadlineDate)) && (eventClassCodeList.contains(eventCode))){
                                items.add(eventData);
                            }
                        }
                        if (items.isEmpty()){
                            text0.setText("No have any event on this date");
                            text0.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else{
                            text0.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void initializeUI() {
        recyclerView = v.findViewById(R.id.RVForScheduleMenu);
        text0 = v.findViewById(R.id.text0);
        calender = v.findViewById(R.id.calender);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new student_class_event_list_adapter(getActivity(),items,this));
        text0.setText("No date selected, please select a date for see event schedule");

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


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            updateLayout(R.layout.fragment_student_schedule_menu);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            updateLayout(R.layout.fragment_student_schedule_menu);
        }
    }


    private void updateLayout(int layoutId) {
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        v = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        initializeUI();
        initializeListener();
        parent.addView(v);
    }



}