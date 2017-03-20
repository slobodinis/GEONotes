package com.apps.geo.notes;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.dialogs.MenuDialog;
import com.apps.geo.notes.fragments.AddPointFragment;
import com.apps.geo.notes.fragments.MainFragment;
import com.apps.geo.notes.geo.LocationTracking;
import com.apps.geo.notes.pojo.PointInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMapManager mapManager;
    private MainFragment mainFragment;
    private boolean changePoint = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //TODO <remove>
        PointInfoDBManager dbManager = new PointInfoDBManager(this);
        ArrayList<PointInfo> points = dbManager.getAllPoints();
        if (points.isEmpty()) {
            PointInfo point = new PointInfo("SUSU", "acabac", 55.158926, 61.365527, 200);
            dbManager.insertPoint(point);
            point = new PointInfo("SUSU2", "acabac", 55.158926, 61.375527, 200);
            dbManager.insertPoint(point);
        }
        //TODO </remove>

        LocationTracking.startLocationTracking(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapManager = new GoogleMapManager(this, googleMap);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(55.158926, 61.375527)));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(1));

        googleMap.setMyLocationEnabled(true);

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return LayoutInflater.from(MainActivity.this).inflate(R.layout.info_window_layout, null);
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

                int id = Integer.parseInt(marker.getSnippet());
                PointInfoDBManager dbManager = new PointInfoDBManager(MainActivity.this);
                PointInfo point = dbManager.getPointById(id);
                mapManager.removePoint(point);
            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!changePoint) {
                    MenuDialog menuDialog = new MenuDialog();
                    menuDialog.setLatLng(latLng);
                    menuDialog.show(getFragmentManager(), "");
                } else
                {
                    getSupportFragmentManager().beginTransaction()
                            .hide(mainFragment)
                            .commit();
                    getSupportFragmentManager().popBackStack();
                    AddPointFragment addPointFragment = (AddPointFragment) getSupportFragmentManager().findFragmentById(R.id.main_activity_root);
                    addPointFragment.setPoint(latLng);
                    changePoint = false;
                }
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

    public void setChangePoint(boolean changePoint)
    {
        this.changePoint = changePoint;
    }

    @Override
    public void onBackPressed()
    {
        if (changePoint)
            changePoint = false;
        super.onBackPressed();
    }
}
