package com.apps.geo.notes;

import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.fragments.AddPointFragment;
import com.apps.geo.notes.fragments.MainFragment;
import com.apps.geo.notes.fragments.adapters.MapClickAdapter;
import com.apps.geo.notes.geo.LocationTracking;
import com.apps.geo.notes.pojo.PointInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import static android.R.attr.id;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMapManager mapManager;
    private MapClickAdapter mapClickAdapter;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.main_activity_root) != null) {
            if (savedInstanceState != null) {
                return;
            }
            mainFragment = new MainFragment();
            mainFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity_root, mainFragment).commit();
        }

        LocationTracking.startLocationTracking(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapManager = new GoogleMapManager(this, googleMap);
        mapClickAdapter = new MapClickAdapter(false, mapManager, mainFragment) {
            @Override
            protected FragmentManager getFragmentManager() {
                return MainActivity.this.getFragmentManager();
            }

            @Override
            protected android.support.v4.app.FragmentManager getSupportFragmentManager() {
                return MainActivity.this.getSupportFragmentManager();
            }
        };

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(55.158926, 61.375527)));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(12));

        googleMap.setMyLocationEnabled(true);

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                PointInfo pointInfo = fromMarker(marker);
                if (pointInfo == null)
                    return null;
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.info_window_layout, null);
                ((TextView) view.findViewById(R.id.point_name)).setText(pointInfo.getName());
                return view;
            }
        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
            }
        });
        googleMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                marker.hideInfoWindow();
                PointInfo pointInfo = fromMarker(marker);
                if (pointInfo != null) {
                    AddPointFragment addPointFragment = new AddPointFragment();
                    addPointFragment.setPointInfo(pointInfo);
                    getSupportFragmentManager().beginTransaction()
                            .hide(mainFragment)
                            .add(R.id.main_activity_root, addPointFragment)
                            .addToBackStack("stack")
                            .commit();
                }
            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mapClickAdapter.performClick(latLng);
            }
        });
    }

    public GoogleMapManager getMapManager()
    {
        return mapManager;
    }

    public MainFragment getMainFragment()
    {
        return mainFragment;
    }

    public MapClickAdapter getMapClickAdapter(){
        return mapClickAdapter;
    }

    @Override
    public void onBackPressed(){
        mapClickAdapter.finishTargeting();
        super.onBackPressed();
    }

    private PointInfo fromMarker(Marker marker) {
        if (marker.getSnippet() == null)
            return null;
        int id = Integer.parseInt(marker.getSnippet());
        PointInfoDBManager dbManager = new PointInfoDBManager(MainActivity.this);
        return dbManager.getPointById(id);
    }
}
