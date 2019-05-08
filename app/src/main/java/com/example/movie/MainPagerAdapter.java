package com.example.movie;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class MainPagerAdapter extends FragmentPagerAdapter {
    ItemFragment fr0,fr1,fr2;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        fr0 = new ItemFragment();
        fr0.id = 0;

        fr1 = new ItemFragment();
        fr1.id = 1;

        fr2 = new ItemFragment();
        fr2.id = 2;
    }

    @Override
    public Fragment getItem(int i) {
        Log.d("OLUSTU","FR");
        switch (i){
            case 0:
                return fr0;
            case 1:
                return fr1;
            case 2:
                return fr2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Son Çıkanlar";
            case 1: return "Popüler";
            case 2: return "En Çok Puan Alanlar";
            default: return null;
        }
    }
}
