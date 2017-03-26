package com.apps.geo.notes.fragments.adapters;

import com.apps.geo.notes.GoogleMapManager;
import com.apps.geo.notes.dialogs.MenuDialog;
import com.apps.geo.notes.fragments.MainFragment;
import com.google.android.gms.maps.model.LatLng;

public abstract class MapClickAdapter {
    private boolean mTargeting;
    private GoogleMapManager mMapManager;
    private MainFragment mMainFragment;
    private LatLng mPoint;

    public MapClickAdapter(boolean targeting, GoogleMapManager mapManager, MainFragment fragment) {
        mTargeting = targeting;
        mMapManager = mapManager;
        mMainFragment = fragment;
    }

    public void performClick(LatLng latLng){
        if (mTargeting){
            performTargetingClick(latLng);
        } else {
            performUsualClick(latLng);
        }
    }

    private void performTargetingClick(LatLng latLng){
        mPoint = latLng;
        mMapManager.setTargetingMaker(latLng);
    }

    private void performUsualClick(LatLng latLng){
        MenuDialog menuDialog = new MenuDialog();
        menuDialog.setLatLng(latLng);
        menuDialog.show(getFragmentManager(), "");
    }

    public void startTargeting(LatLng startPoint){
        mMapManager.clearMap();
        if (!mTargeting) {
            mTargeting = true;
            mMainFragment.targetingMode();
            if (startPoint != null){
                performTargetingClick(startPoint);
            }
        }
    }

    public void finishTargeting(){
        mMapManager.update();
        if (mTargeting){
            mTargeting = false;
            mMainFragment.normalMode();
        }

    }

    protected abstract android.app.FragmentManager getFragmentManager();
    protected abstract android.support.v4.app.FragmentManager getSupportFragmentManager();

    public LatLng getChosenPoint() {
        return mPoint;
    }
}
