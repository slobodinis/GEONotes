package com.apps.geo.notes;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;

import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.pojo.PointInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class GoogleMapManager {

    private class PointInfoView {
        public Marker marker;
        public Circle circle;
    }

    private Context context;
    private GoogleMap map;

    private SparseArray<PointInfoView> views;

    public GoogleMapManager(Context context, GoogleMap map) {
        this.context = context;
        this.map = map;
        views = new SparseArray<>();
        update();
    }

    public void update() {
        map.clear();
        views.clear();

        PointInfoDBManager dbManager = new PointInfoDBManager(context);
        ArrayList<PointInfo> points = dbManager.getAllPoints();
        for (PointInfo point : points) {
            addPoint(point);
        }
    }

    public void addPoint(PointInfo point) {
        LatLng pos = new LatLng(point.getLattitude(), point.getLongitude());
        Marker marker = map.addMarker
                (new MarkerOptions()
                        .position(pos)
                        .title(point.getName())
                        .snippet(point.getId()+"")
                );
        Circle circle = map.addCircle
                (new CircleOptions()
                        .center(pos)
                        .radius(point.getRadius())
                        .strokeColor(getCircleColor())
                        .strokeWidth(2)
                );
        PointInfoView view = new PointInfoView();
        view.circle = circle;
        view.marker = marker;
        views.put(point.getId(), view);
    }

    public void removePoint(PointInfo point) {
        PointInfoView view = views.get(point.getId());
        if (view != null) {
            view.marker.remove();
            view.circle.remove();
            views.remove(point.getId());
        }
    }

    public void changePointState(PointInfo point) {
        PointInfoView view = views.get(point.getId());
        if (view != null) {
            //TODO change color, radius
        }
    }

    private int getCircleColor() {
        return Color.GREEN;
    }

}
