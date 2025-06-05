package com.example.zyra.studentAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.zyra.studentFragmentAndActivity.student_event_list_fragment;
import com.example.zyra.studentFragmentAndActivity.student_material_list_fragment;

public class student_class_menu_viewpager_adapter extends FragmentStateAdapter {
    public student_class_menu_viewpager_adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new student_event_list_fragment();
        }
        return new student_material_list_fragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
