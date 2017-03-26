package com.apps.geo.notes.fragments;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.apps.geo.notes.MainActivity;
import com.apps.geo.notes.R;
import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.fragments.adapters.MapClickAdapter;
import com.apps.geo.notes.pojo.PointInfo;
import com.google.android.gms.maps.model.LatLng;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.Date;

public class AddPointFragment extends Fragment {
    private LatLng point;
    private PointInfo pointInfo;
    private TextView coordinates;
    private EditText name;
    private EditText description;
    private RangeSeekBar radius;
    private CheckBox alarmCheck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_point_fragment, null);
        if (point == null)
            point = new LatLng(0,0);
        name = (EditText) view.findViewById(R.id.name_edit);
        description = (EditText) view.findViewById(R.id.description_edit);
        radius = (RangeSeekBar) view.findViewById(R.id.radius_bar);
        coordinates = (TextView) view.findViewById(R.id.coordinates_text);
        alarmCheck = (CheckBox) view .findViewById(R.id.enable_alarm);
        setPoint(point);
        if (pointInfo != null){
            name.setText(pointInfo.getName());
            description.setText(pointInfo.getDescription());
            alarmCheck.setChecked(pointInfo.isActive());
            radius.setSelectedMaxValue(pointInfo.getRadius());
        }
        final FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add_point_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PointInfoDBManager pointInfoDBManager = new PointInfoDBManager(getActivity());
                    if (pointInfo == null){
                        pointInfo = new PointInfo();
                    }
                    pointInfo.setName(name.getText().toString());
                    pointInfo.setDescription(description.getText().toString());
                    pointInfo.setActive(alarmCheck.isChecked());
                    pointInfo.setLatitude(point.latitude);
                    pointInfo.setLongitude(point.longitude);
                    pointInfo.setRadius(radius.getSelectedMaxValue().doubleValue());
                    if (pointInfo.getId() != 0) {
                        pointInfoDBManager.updatePointById(pointInfo);
                        ((MainActivity)getActivity()).getMapManager().update();
                    } else {
                        pointInfoDBManager.insertPoint(pointInfo);
                        ((MainActivity)getActivity()).getMapManager().addPoint(pointInfo);
                    }

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
                if (pointInfo != null) {
                    mainActivity.getMapManager().centerOnPoint(pointInfo);
                }
                MainFragment mainFragment = mainActivity.getMainFragment();
                mainFragment.setItem(1);
                mainFragment.targetingMode();
                MapClickAdapter adapter = mainActivity.getMapClickAdapter();
                if (adapter != null){
                    adapter.startTargeting(point);
                }
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
        if (pointInfo != null){
            pointInfo.setLatitude(point.latitude);
            pointInfo.setLongitude(point.longitude);
        }
    }

    public void setPointInfo(PointInfo info){
        this.pointInfo = info;
        setPoint(new LatLng(info.getLatitude(), info.getLongitude()));
    }
}
