package com.apps.geo.notes;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.apps.geo.notes.fragments.MainFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.main_activity_root) != null) {
            if (savedInstanceState != null) {
                return;
            }
            MainFragment mainFragment = new MainFragment();
            mainFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity_root, mainFragment).commit();
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);

        final LatLng pos1 = new LatLng(55.158926, 61.375527);
        map.addMarker(new MarkerOptions().position(pos1));

        final LatLng pos2 = new LatLng(55.158926, 61.365527);
        map.addMarker(new MarkerOptions().position(pos2).title("SUSU"));
        map.moveCamera(CameraUpdateFactory.newLatLng(pos2));
        map.moveCamera(CameraUpdateFactory.zoomTo(14));

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                if (marker.getPosition().equals(pos2))
                    return null;
                return LayoutInflater.from(MainActivity.this).inflate(R.layout.info_window_layout, null);
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.getPosition().equals(pos2))
                    return;
                marker.hideInfoWindow();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "acabac", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
