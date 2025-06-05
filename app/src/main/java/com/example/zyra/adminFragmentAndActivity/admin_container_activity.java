package com.example.zyra.adminFragmentAndActivity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.zyra.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class admin_container_activity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_container);

        initializeUI();
        initializeListener();
    }

    private void initializeListener() {
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.classMenu) {
                setFragment(new admin_class_list_fragment());
                return true;
            } else if (item.getItemId() == R.id.eventMenu) {
                setFragment(new admin_event_list_fragment());
                return true;
            }
            return false;
        });
    }

    private void initializeUI() {
        bottomNavigation = findViewById(R.id.adminBottomNavigation);
        setFragment(new admin_class_list_fragment());
        bottomNavigation.setSelectedItemId(R.id.classMenu);
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.adminFrameLayoutContainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.adminFrameLayoutContainer);

        if (currentFragment instanceof admin_class_list_fragment) {
            bottomNavigation.setSelectedItemId(R.id.classMenu);
        } else if (currentFragment instanceof admin_event_list_fragment) {
            bottomNavigation.setSelectedItemId(R.id.eventMenu);
        }
    }
}