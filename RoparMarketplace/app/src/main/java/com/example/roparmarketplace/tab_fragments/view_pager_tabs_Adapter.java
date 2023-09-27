package com.example.roparmarketplace.tab_fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.roparmarketplace.breakfastFragment.breakfast_fragment;
import com.example.roparmarketplace.tab_fragments.dinner_fragment;
import com.example.roparmarketplace.tab_fragments.lunch_fragment;

public class view_pager_tabs_Adapter extends FragmentPagerAdapter {



    public view_pager_tabs_Adapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0:return new breakfast_fragment();
            case 1:return new lunch_fragment();
            case 2:return new dinner_fragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch(position)
        {
            case 0: return "Breakfast";
            case 1: return "Lunch";
            case 2: return "Dinner";
        }
        return super.getPageTitle(position);
    }

}
