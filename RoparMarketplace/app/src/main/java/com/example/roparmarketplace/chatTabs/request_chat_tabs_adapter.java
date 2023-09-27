package com.example.roparmarketplace.chatTabs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.roparmarketplace.ChatFragment;
import com.example.roparmarketplace.myCoupons.my_coupons_fragment;

public class request_chat_tabs_adapter extends FragmentPagerAdapter {


    public request_chat_tabs_adapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0:return new ChatFragment();
            case 1:return new my_coupons_fragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position)
        {
            case 0:return "Chats";
            case 1:return "My Coupons";
        }
        return super.getPageTitle(position);

    }
}
