package com.example.zyra.adminFragmentAndActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zyra.R;
import com.example.zyra.adminAdapter.admin_material_list_adapter;
import com.example.zyra.studentFragmentAndActivity.student_material_details_activity;
import com.example.zyra.modelData.subject;
import com.example.zyra.modelData.material;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class admin_material_list_activity extends AppCompatActivity implements admin_material_list_adapter.OnClickItemListener {

    RecyclerView recyclerView;
    TextView materialClassName, materialClassCode, materialClassLecturer,text0;
    List<material> items = new ArrayList<>();
    RecyclerView.Adapter adapter;
    subject intentClassDetails;
    MaterialButton addMaterialButton;
    ImageButton materialListBackButton;
    DatabaseReference classRef, materialRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_material_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeUI();
        initializeFireBase();
        initializeListener();
    }

    private void initializeFireBase() {
        classRef = FirebaseDatabase.getInstance().getReference("classes");
        materialRef = FirebaseDatabase.getInstance().getReference("materials");

        getAllMaterialList();
    }

    private void getAllMaterialList() {
        items.clear();
        String selectedClassCode = intentClassDetails.getClassCode();
        materialRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    boolean classCodeExists = false;
                    for (DataSnapshot materialSnapshot : snapshot.getChildren()){
                        material materialData = materialSnapshot.getValue(material.class);
                        if (materialData != null && selectedClassCode.equals(materialData.getMaterialClassCode())){
                            items.add(materialData);
                            classCodeExists = true;
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
                Toast.makeText(admin_material_list_activity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeListener() {
        addMaterialButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, admin_add_material_activity.class);
            intent.putExtra("material_class_code",intentClassDetails.getClassCode());
            startActivity(intent);
        });

        materialListBackButton.setOnClickListener(view -> {
            finish();
        });
    }

    private void initializeUI() {
        recyclerView = findViewById(R.id.adminRVForMaterialList);
        materialClassName = findViewById(R.id.adminMaterialClassName);
        materialClassCode = findViewById(R.id.adminMaterialClassCode);
        materialClassLecturer = findViewById(R.id.adminMaterialLecturer);
        addMaterialButton = findViewById(R.id.adminAddMaterialClassButton);
        materialListBackButton = findViewById(R.id.materialListBackButton);
        text0 = findViewById(R.id.text0);

        intentClassDetails = (subject) getIntent().getSerializableExtra("material_list");

        materialClassName.setText(intentClassDetails.getClassName());
        materialClassCode.setText(intentClassDetails.getClassCode());
        materialClassLecturer.setText(intentClassDetails.getLecturerName());

        adapter = new admin_material_list_adapter(this,items,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(material item) {
        Intent intent = new Intent(this, student_material_details_activity.class);
        intent.putExtra("material_details",item);
        startActivity(intent);
    }

    @Override
    public void onItemEditClicked(material item) {
        Intent intent = new Intent(this, admin_edit_material_activity.class);
        intent.putExtra("material_id",item.getMaterialId());
        startActivity(intent);
    }

    @Override
    public void onItemDeleteClicked(material item) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.delete_icon)
                .setTitle("Delete material")
                .setMessage("Do you really want to delete this material?")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteMaterial(item);
                        Toast.makeText(admin_material_list_activity.this, (item.getMaterialName() + " deleted"), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(admin_material_list_activity.this, "Cancel delete material", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void deleteMaterial(material item) {
        String materialId = item.getMaterialId();
        DatabaseReference materialIdRef = materialRef.child(materialId);
        materialIdRef.removeValue()
                .addOnCompleteListener(task -> {
                    items.remove(item);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, item.getMaterialId() + " deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(task ->{
                    Toast.makeText(this, "Error deleting class ", Toast.LENGTH_SHORT).show();
                });
    }
}