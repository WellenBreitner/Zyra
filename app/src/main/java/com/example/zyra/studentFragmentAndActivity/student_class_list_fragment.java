package com.example.zyra.studentFragmentAndActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zyra.modelData.subject;
import com.example.zyra.R;
import com.example.zyra.studentAdapter.student_class_list_adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;


public class student_class_list_fragment extends Fragment implements student_class_list_adapter.OnItemClickListener {

    View v;
    List<subject> items = new ArrayList<>();
    RecyclerView recyclerview;
    RecyclerView.Adapter adapter;
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

        v = inflater.inflate(R.layout.fragment_student_class_list, container, false);
        initializeUI();
        GetRegisteredSubject();

        return v;
    }

    private void GetRegisteredSubject() {
        items.clear();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID;
        if (currentUser == null){
            userID = "";
            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
        }else{
            userID = currentUser.getUid();
        }


        DatabaseReference registerSubjectRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("registeredSubjects");
        registerSubjectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            List<String> registerSubjectList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot registerSubjectSnapshot : snapshot.getChildren()) {
                        String registerSubjectData = registerSubjectSnapshot.getValue(String.class);
                        if (registerSubjectData != null){
                            registerSubjectList.add(registerSubjectData);
                        }
                    }
                }else {
                    Toast.makeText(getContext(), "Class not found.", Toast.LENGTH_SHORT).show();
                }

                DatabaseReference getRegisterSubject =FirebaseDatabase.getInstance().getReference("classes");
                getRegisterSubject.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot registerSubject : snapshot.getChildren()){
                                subject subjectData = registerSubject.getValue(subject.class);
                                String subjectCode = subjectData.getClassCode();
                                if (registerSubjectList.contains(subjectCode)){
                                    items.add(subjectData);
                                }
                            }

                            if (items.isEmpty()) {
                                text0.setVisibility(View.VISIBLE);
                                recyclerview.setVisibility(View.GONE);
                            } else {
                                text0.setVisibility(View.GONE);
                                recyclerview.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            text0.setVisibility(View.VISIBLE);
                            recyclerview.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initializeUI() {
        recyclerview = v.findViewById(R.id.RVForClassList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new student_class_list_adapter(getActivity(),items,this);
        recyclerview.setAdapter(adapter);
        text0 = v.findViewById(R.id.text0);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(subject item) {
        Intent intent = new Intent(getContext(), student_class_menu_activity.class);
        intent.putExtra("student_material_list", item);
        startActivity(intent);
    }
}