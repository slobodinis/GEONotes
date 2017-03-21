package com.apps.geo.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.apps.geo.notes.MainActivity;
import com.apps.geo.notes.R;
import com.apps.geo.notes.fragments.viewpager.CustomViewPager;
import com.google.android.gms.maps.SupportMapFragment;

public class MainFragment extends Fragment{
    private SupportMapFragment mMapFragment;
    private NoteListFragment noteListFragment;
    private CustomViewPager pager;
    private int item = 0;

    public MainFragment() {
    }

    private SupportMapFragment loadMap() {
        if (mMapFragment == null){
            mMapFragment = SupportMapFragment.newInstance();
            mMapFragment.getMapAsync((MainActivity) getActivity());
        }
        return mMapFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pager, null);
        pager = (CustomViewPager) rootView.findViewById(R.id.pager);
        NavigationAdapter adapter = new NavigationAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(item);
        pager.requestTransparentRegion(pager);
        return rootView;
    }

    private class NavigationAdapter extends FragmentPagerAdapter{

        NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case 0:
                    return MainFragment.this.getNoteListFragment();
                case 1:
                    return MainFragment.this.loadMap();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public void setItem(int item)
    {
        this.item = item;
        pager.setCurrentItem(item);
    }

    public NoteListFragment getNoteListFragment()
    {
        if (noteListFragment == null)
            noteListFragment = new NoteListFragment();
        return noteListFragment;
    }

    public void enableSwipe()
    {
        pager.isSwipeEnabled = true;
    }

    public void disableSwipe()
    {
        pager.isSwipeEnabled = false;
    }
}
