package com.example.fitness_tracker_app.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.fitness_tracker_app.run.current.CurrentRunFragment;
import com.example.fitness_tracker_app.run.history.HistoryRunFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CurrentRunFragment();
        } else {
            return new HistoryRunFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
