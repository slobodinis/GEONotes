package com.apps.geo.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.apps.geo.notes.MainActivity;
import com.apps.geo.notes.R;
import com.apps.geo.notes.fragments.adapters.MapClickAdapter;
import com.apps.geo.notes.fragments.viewpager.CustomViewPager;
import com.google.android.gms.maps.SupportMapFragment;

public class MainFragment extends Fragment{
    private SupportMapFragment mapFragment;
    private NoteListFragment noteListFragment;
    private FloatingActionButton fab;
    private CustomViewPager pager;
    private int item = 0;

    public MainFragment() {
    }

    private SupportMapFragment loadMap() {
        if (mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync((MainActivity) getActivity());
        }
        return mapFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout rootView = (RelativeLayout) inflater.inflate(R.layout.main_fragment, null);
        pager = (CustomViewPager) rootView.findViewById(R.id.pager);
        fab = (FloatingActionButton) rootView.findViewById(R.id.confirm_target_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction()
                        .hide(MainFragment.this)
                        .commit();
                manager.popBackStack();
                AddPointFragment addPointFragment = (AddPointFragment) manager
                        .findFragmentById(R.id.main_activity_root);
                MapClickAdapter adapter = ((MainActivity) getActivity()).getMapClickAdapter();
                addPointFragment.setPoint(adapter.getChosenPoint());
                adapter.finishTargeting();
            }
        });
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

    public void normalMode(){
        fab.setVisibility(View.GONE);
        pager.isSwipeEnabled = true;
    }

    public void targetingMode(){
        fab.setVisibility(View.VISIBLE);
        pager.isSwipeEnabled = false;
    }
}
