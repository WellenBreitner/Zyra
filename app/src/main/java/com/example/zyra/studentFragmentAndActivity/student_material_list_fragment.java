package com.example.zyra.studentFragmentAndActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.zyra.studentAdapter.student_material_list_adapter;
import com.example.zyra.modelData.material;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class student_material_list_fragment extends Fragment implements student_material_list_adapter.OnItemClickListener {

    View v;
    List<material> items = new ArrayList<>();
    RecyclerView recyclerview;
    TextView text0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(container != null){
            container.removeAllViews();
        }

        v =  inflater.inflate(R.layout.fragment_student_material_list_fragment, container, false);
        initializeUI();
        return v;
    }

    private void initializeUI() {
        recyclerview = v.findViewById(R.id.RVForMaterialList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(new student_material_list_adapter(getContext(),items,this));
        text0 = v.findViewById(R.id.text0);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("subject_code_student", Context.MODE_PRIVATE);
        String subjectCode = sharedPreferences.getString("subject_code","");

        getAllTheMaterial(subjectCode);
    }

    @Override
    public void onClick(material items) {
        Intent intent = new Intent(getContext(), student_material_details_activity.class);
        intent.putExtra("material_details",items);
        startActivity(intent);
    }

    private void getAllTheMaterial(String subjectCode) {
        items.clear();

        DatabaseReference materialRef = FirebaseDatabase.getInstance().getReference("materials");

        materialRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot materialSnapShot : snapshot.getChildren()){
                        material materialData = materialSnapShot.getValue(material.class);
                        String materialCode = materialData.getMaterialClassCode();

                        if (materialCode !=null && materialCode.equals(subjectCode)){
                            items.add(materialData);
                        }
                    }if (items.isEmpty()) {
                        text0.setVisibility(View.VISIBLE);
                        recyclerview.setVisibility(View.GONE);
                    } else {
                        text0.setVisibility(View.GONE);
                        recyclerview.setVisibility(View.VISIBLE);
                    }
                    recyclerview.getAdapter().notifyDataSetChanged();
                }else{
                    Toast.makeText(getContext(), "Materials not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: error.getMessage()", Toast.LENGTH_SHORT).show();
            }
        });
    }
}