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
import com.example.zyra.adminAdapter.admin_class_list_adapter;
import com.example.zyra.modelData.subject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class admin_class_list_fragment extends Fragment implements admin_class_list_adapter.OnItemClickListener {

    RecyclerView recyclerView;
    Button addClassButton;
    View v;
    RecyclerView.Adapter adapter;
    TextView text0;
    List<subject> items = new ArrayList<>();
    private DatabaseReference classRef;

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

        v = inflater.inflate(R.layout.fragment_admin_class_list, container, false);

        initializeUI();
        initializeFireBase();
        initializeListener();

        return v;
    }

    private void initializeFireBase() {
        classRef = FirebaseDatabase.getInstance().getReference("classes");

        getAllClassList();
    }

    private void getAllClassList() {
        items.clear();
        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                        subject subjectData = subjectSnapshot.getValue(subject.class);
                        if (subjectData != null) {
                            items.add(subjectData);
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

                } else {
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

    private void initializeListener() {
        addClassButton.setOnClickListener(view -> moveToAddClassPage());
    }

    private void initializeUI() {
        text0 = v.findViewById(R.id.text0);
        recyclerView = v.findViewById(R.id.AdminRVForClassList);
        addClassButton = v.findViewById(R.id.adminAddClassButton);
        adapter = new admin_class_list_adapter(getContext(), items, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void moveToAddClassPage() {
        Intent intent = new Intent(getContext(), admin_add_class_activity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(subject item) {
        Intent intent = new Intent(getContext(), admin_material_list_activity.class);
        intent.putExtra("material_list", item);
        startActivity(intent);
    }

    @Override
    public void onItemEditClicked(subject item) {
        Intent intent = new Intent(getContext(), admin_edit_class_activity.class);
        intent.putExtra("edit_class", item);
        startActivity(intent);
    }

    @Override
    public void onItemDeletedClicked(subject item) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.delete_icon)
                .setTitle("Delete class")
                .setMessage("Do you really want to delete this class?")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteClass(item);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Cancel delete class", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void deleteClass(subject item) {
        String subjectId = item.getSubjectId();
        DatabaseReference classToDeleteRef = classRef.child(subjectId);

        classToDeleteRef.removeValue()
                .addOnCompleteListener(aVoid -> {
                    items.remove(item);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), item.getClassName() + " deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error deleting class ", Toast.LENGTH_SHORT).show();
                });
    }





}