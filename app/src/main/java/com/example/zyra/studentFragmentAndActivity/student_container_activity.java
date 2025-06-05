package com.example.zyra.studentFragmentAndActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.zyra.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class student_container_activity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_container);

        bottomNavigation = findViewById(R.id.studentBottomNavigation);

        setFragment(new student_dashboard_fragment());
        bottomNavigation.setSelectedItemId(R.id.homeMenu);

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.homeMenu) {
                setFragment(new student_dashboard_fragment());
                return true;
            } else if (item.getItemId() == R.id.scheduleMenu) {
                setFragment(new student_schedule_menu_fragment());
                return true;
            } else if (item.getItemId() == R.id.enrollClassMenu) {
                DialogFragment dialog = new student_enroll_class_fragment();
                dialog.show(getSupportFragmentManager(),"dialog");
                return true;
            } else if (item.getItemId() == R.id.classMenu) {
                    setFragment(new student_class_list_fragment());
                return true;
            } else if (item.getItemId() == R.id.settingMenu) {
                setFragment(new student_settings_fragment());
                return true;
            }
            return false;
        });


    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.studentFrameLayoutContainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.studentFrameLayoutContainer);

        if (currentFragment instanceof student_dashboard_fragment) {
            bottomNavigation.setSelectedItemId(R.id.homeMenu);
        } else if (currentFragment instanceof student_schedule_menu_fragment) {
            bottomNavigation.setSelectedItemId(R.id.scheduleMenu);
        } else if (currentFragment instanceof student_class_list_fragment) {
            bottomNavigation.setSelectedItemId(R.id.classMenu);
        } else if (currentFragment instanceof student_enroll_class_fragment) {
            bottomNavigation.setSelectedItemId(R.id.enrollClassMenu);
        } else if (currentFragment instanceof student_settings_fragment) {
            bottomNavigation.setSelectedItemId(R.id.settingMenu);
        }
    }

}