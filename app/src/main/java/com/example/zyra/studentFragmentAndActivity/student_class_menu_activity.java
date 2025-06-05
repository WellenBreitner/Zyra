package com.example.zyra.studentFragmentAndActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.zyra.R;
import com.example.zyra.studentAdapter.student_class_menu_viewpager_adapter;
import com.example.zyra.modelData.subject;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class student_class_menu_activity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ImageButton materialListBackButton;
    TextView studentMaterialClassName,studentMaterialClassCode,studentMaterialLecturer;
    subject intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_class_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeUI();
        initializeListener();
    }

    private void initializeListener() {
        materialListBackButton.setOnClickListener(view -> {
            finish();
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }

    private void initializeUI() {
        intent = (subject) getIntent().getSerializableExtra("student_material_list");
        studentMaterialClassName = findViewById(R.id.studentMaterialClassName);
        studentMaterialClassCode = findViewById(R.id.studentMaterialClassCode);
        studentMaterialLecturer = findViewById(R.id.studentMaterialLecturer);
        materialListBackButton = findViewById(R.id.studentMaterialListBackButton);

        studentMaterialClassName.setText(intent.getClassName());
        studentMaterialClassCode.setText(intent.getClassCode());
        studentMaterialLecturer.setText(intent.getLecturerName());

        tabLayout = findViewById(R.id.tabLayoutForClassMenu2);
        viewPager2 = findViewById(R.id.viewPagerForClassMenu2);
        viewPager2.setAdapter(new student_class_menu_viewpager_adapter(this));

        SharedPreferences sharedPreferences = this.getSharedPreferences("subject_code_student", Context.MODE_PRIVATE);
        SharedPreferences.Editor material = sharedPreferences.edit();
        material.putString("subject_code",intent.getClassCode());
        material.apply();


    }




}