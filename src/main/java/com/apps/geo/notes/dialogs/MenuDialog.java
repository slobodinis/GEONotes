package com.apps.geo.notes.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.apps.geo.notes.MainActivity;
import com.apps.geo.notes.R;
import com.apps.geo.notes.fragments.AddPointFragment;
import com.google.android.gms.maps.model.LatLng;

public class MenuDialog extends DialogFragment {

    private LatLng point;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_dialog, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        final Button addPointButton = (Button)view.findViewById(R.id.addPointButton);
        addPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPointFragment addPointFragment = new AddPointFragment();
                addPointFragment.setPoint(point);
                ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_root,addPointFragment)
                        .addToBackStack("stack")
                        .commit();
                dismiss();
            }
        });
        return view;
    }

    public void setLatLng(LatLng point)
    {
        this.point = point;
    }
}
