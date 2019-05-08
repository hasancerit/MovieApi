package com.example.movie;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class VizyonPager extends FragmentPagerAdapter {
    ArrayList<MainFilm> filmler;

    public VizyonPager(FragmentManager fm, ArrayList<MainFilm> filmler) {
        super(fm);
        this.filmler = filmler;
        Log.d("HASAN CERİT","Vizyon Pager cons");
    }

    @Override
    public Fragment getItem(int i) {
        Log.d("HASAN CERİT","Vizyon Pager getItem**"+i);
        FragmentVizyon fragment =FragmentVizyon.newInstance(filmler.get(i));
        return fragment;
    }

    @Override
    public int getCount() {
        return filmler.size();
    }
}
