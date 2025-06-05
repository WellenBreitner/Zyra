package com.example.zyra.studentFragmentAndActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zyra.ROOM.Database;
import com.example.zyra.ROOM.EventRoom;
import com.example.zyra.studentAdapter.student_class_event_list_adapter;
import com.example.zyra.modelData.event;
import com.example.zyra.modelData.student;
import com.example.zyra.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;


public class student_dashboard_fragment extends Fragment implements student_class_event_list_adapter.OnClickItemListener {
    View v;
    TextView studentName, studentStatus, text0;
    ShapeableImageView studentImage;
    CardView downloadedEvent;
    RecyclerView recyclerView;
    List<event> items = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        v = inflater.inflate(R.layout.fragment_student_dashboard, container, false);

        initializeUI();
        initializeListener();
        return v;
    }

    private void initializeListener() {
        downloadedEvent.setOnClickListener(view -> offlineEventButton());
    }

    private void initializeUI() {
        downloadedEvent = v.findViewById(R.id.saved_event);
        studentName = v.findViewById(R.id.studentNameText);
        studentStatus = v.findViewById(R.id.studentStatusText);
        text0 = v.findViewById(R.id.text0);

        recyclerView = v.findViewById(R.id.recyclerViewForEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new student_class_event_list_adapter(getActivity(), items, this));
        recyclerView.setHasFixedSize(true);

        getUserInformation();
        getRegisteredSubjectEvent();
    }

    private void getUserInformation() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getActivity(), "You not login", Toast.LENGTH_SHORT).show();
        } else {
            String userID = currentUser.getUid();
            DatabaseReference getUserInfo = FirebaseDatabase.getInstance().getReference("users");
            getUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                            student studentData = studentSnapshot.getValue(student.class);
                            String studentID = studentSnapshot.getKey();
                            if (studentData != null && userID.equals(studentID)) {
                                studentName.setText(String.format("Name: %s", studentData.getName()));
                                studentStatus.setText(String.format("Status: %s ", studentData.getStatus()));
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Database not founded", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getRegisteredSubjectEvent() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getActivity(), "You not login", Toast.LENGTH_SHORT).show();
        } else {
            String userID = currentUser.getUid();
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
                            if ((eventData != null) && (eventClassCodeList.contains(eventCode))){
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
        }
    }


    private void offlineEventButton() {
        Intent intent = new Intent(getActivity(), student_all_downloaded_event_activity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(event item) {
        Intent intent = new Intent(getContext(), student_event_details_activity.class);
        intent.putExtra("event_detail", item);
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