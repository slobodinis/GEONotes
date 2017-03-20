package com.apps.geo.notes.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.geo.notes.MainActivity;
import com.apps.geo.notes.R;
import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.pojo.PointInfo;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class AddPointFragment extends Fragment {

    private LatLng point;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_point_fragment, null);
        final EditText name = (EditText) view.findViewById(R.id.nameEdit);
        final EditText description = (EditText) view.findViewById(R.id.descriptionEdit);
        final EditText term = (EditText) view.findViewById(R.id.termEdit);
        final EditText radius = (EditText) view.findViewById(R.id.radiusEdit);
        TextView lat = (TextView)view.findViewById(R.id.lattitudeText);
        lat.setText(point.latitude + "");
        TextView lng = (TextView)view.findViewById(R.id.longitudeText);
        lng.setText(point.longitude + "");
        final Button add = (Button) view.findViewById(R.id.addPointButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PointInfo pointInfo = new PointInfo(name.getText().toString(), description.getText().toString(),
                            point.latitude, point.longitude, new Date(), Long.parseLong(term.getText().toString()),
                            Double.parseDouble(radius.getText().toString()));
                    PointInfoDBManager pointInfoDBManager = new PointInfoDBManager(getActivity());
                    pointInfoDBManager.insertPoint(pointInfo);
                    ((MainActivity)getActivity()).getMapManager().addPoint(pointInfo);
                    ((MainActivity)getActivity()).getMapManager().update();
                    getActivity().onBackPressed();
                } catch (Exception e)
                {
                    Log.e("Error",e.getMessage());
                }
            }
        });
        return view;
    }

    public void setPoint(LatLng point)
    {
        this.point = point;
    }
}