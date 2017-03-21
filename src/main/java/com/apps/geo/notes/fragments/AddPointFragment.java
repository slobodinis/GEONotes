package com.apps.geo.notes.fragments;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.apps.geo.notes.MainActivity;
import com.apps.geo.notes.R;
import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.pojo.PointInfo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.Date;

public class AddPointFragment extends Fragment {

    private LatLng point;
    private TextView coordinates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_point_fragment, null);
        if (point == null)
            point = new LatLng(0,0);
        final EditText name = (EditText) view.findViewById(R.id.name_edit);
        final EditText description = (EditText) view.findViewById(R.id.description_edit);
        final RangeSeekBar radius = (RangeSeekBar) view.findViewById(R.id.radius_bar);
        coordinates = (TextView)view.findViewById(R.id.coordinates_text);
        setPoint(point);
        final FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add_point_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PointInfo pointInfo = new PointInfo(name.getText().toString(), description.getText().toString(),
                            point.latitude, point.longitude, new Date(), -1, radius.getSelectedMaxValue().doubleValue());
                    PointInfoDBManager pointInfoDBManager = new PointInfoDBManager(getActivity());
                    pointInfoDBManager.insertPoint(pointInfo);
                    ((MainActivity)getActivity()).getMapManager().addPoint(pointInfo);
                    ((MainActivity)getActivity()).getMapManager().update();
                    MainFragment mainFragment = ((MainActivity)getActivity()).getMainFragment();
                    mainFragment.getNoteListFragment().switchToBaseForm();
                    getActivity().onBackPressed();
                } catch (Exception e)
                {
                    Log.e("Error",e.getMessage());
                }
            }
        });
        ImageButton changePointButton = (ImageButton) view.findViewById(R.id.change_point_button);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity)getActivity();
                MainFragment mainFragment = mainActivity.getMainFragment();
                mainFragment.setItem(1);
                mainFragment.disableSwipe();
                mainActivity.setChangePoint(true);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .hide(AddPointFragment.this)
                        .show(mainFragment)
                        .addToBackStack("stack")
                        .commit();
            }
        };
        changePointButton.setOnClickListener(listener);
        coordinates.setOnClickListener(listener);
        return view;
    }

    public void setPoint(LatLng point){
        this.point = point;
        if (coordinates != null){
            coordinates.setText(String.format("(%.2f; %.2f)", point.latitude, point.longitude));
        }
    }
}
