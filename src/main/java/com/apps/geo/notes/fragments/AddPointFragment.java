package com.apps.geo.notes.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.geo.notes.MainActivity;
import com.apps.geo.notes.R;
import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.fragments.adapters.MapClickAdapter;
import com.apps.geo.notes.pojo.PointInfo;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class AddPointFragment extends Fragment {

    private final int PLACE_PICKER_REQUEST_CODE = 4573;

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
        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.add_point_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()) {
                    Snackbar.make(view, "Название не должно быть пустым", Snackbar.LENGTH_SHORT).show();
                    return;
                }

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
                } catch (Exception e) {
                    e.printStackTrace();
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

        view.findViewById(R.id.search_point_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST_CODE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Ошибка Google Play Services",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                Place place = PlacePicker.getPlace(getContext(), data);
                String resultName = place.getName() == null ? "" : place.getName().toString();
                String resultAddress = place.getAddress() == null ? "" : place.getAddress().toString();
                LatLng resultPos = place.getLatLng();

                if (name != null && name.getText().toString().isEmpty())
                    name.setText(resultName);
                if (description != null && description.getText().toString().isEmpty())
                    description.setText(resultAddress);
                if (resultPos != null)
                    setPoint(resultPos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
