package com.example.zyra.adminFragmentAndActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zyra.R;
import com.example.zyra.adminAdapter.admin_event_list_adapter;
import com.example.zyra.studentFragmentAndActivity.student_event_details_activity;
import com.example.zyra.modelData.event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class admin_event_list_fragment extends Fragment implements admin_event_list_adapter.OnClickItemListener {

    RecyclerView recyclerView;
    Button addClassButton;
    RecyclerView.Adapter adapter;
    TextView text0;
    View v;
    DatabaseReference eventRef;
    List<event> items = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_admin_event_list, container, false);

        initializeUI();
        initializeFireBase();
        initializeListener();

        return v;
    }

    private void initializeFireBase() {
        eventRef = FirebaseDatabase.getInstance().getReference("events");

        getAllEventList();
    }

    private void getAllEventList() {
        items.clear();
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.exists()){
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()){
                        event eventData = eventSnapshot.getValue(event.class);
                        if (eventData !=null){
                            items.add(eventData);
                        }
                    }
                    if (items.isEmpty()) {
                        text0.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        text0.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    text0.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeUI() {
        recyclerView = v.findViewById(R.id.AdminRVForEventList);
        addClassButton = v.findViewById(R.id.adminAddEventButton);
        text0 = v.findViewById(R.id.text0);

        adapter = new admin_event_list_adapter(getContext(),items,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initializeListener() {
        addClassButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), admin_add_event_activity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onItemClicked(event item) {
        Intent intent = new Intent(getContext(), student_event_details_activity.class);
        intent.putExtra("event_detail",item);
        startActivity(intent);
    }

    @Override
    public void onItemEditClicked(event item) {
        Intent intent = new Intent(getContext(),admin_edit_event_activity.class);
        intent.putExtra("event_edit",item);
        startActivity(intent);
    }

    @Override
    public void onItemDeletedClicked(event item) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.delete_icon)
                .setTitle("Delete event")
                .setMessage("Do you really want to delete this event?")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteEvent(item);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getContext(), "Cancel material class", Toast.LENGTH_SHORT).show();
                        }
                    })
                .show();
    }

    private void deleteEvent(event item) {
        String eventId = item.getEventId();
        DatabaseReference eventToDeleteRef = eventRef.child(eventId);

        eventToDeleteRef.removeValue()
                .addOnCompleteListener(aVoid -> {
                    items.remove(item);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), item.getEventName() + " deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error deleting event", Toast.LENGTH_SHORT).show();
                });
    }
}
