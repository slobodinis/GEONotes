package com.apps.geo.notes.geo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.apps.geo.notes.AlarmActivity;
import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.pojo.PointInfo;

import java.util.ArrayList;

public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.wtf("афыафыа", "gsgfdgfdg");
        if (intent != null && intent.getExtras() != null) {
            Object obj = intent.getExtras().get("location");
            if (obj != null) {
                Location location = (Location) obj;
                Log.wtf("location", location.toString());

                PointInfoDBManager dbManager = new PointInfoDBManager(context);
                ArrayList<PointInfo> points = dbManager.getActivePoints();

                float[] dist = new float[1];
                for (PointInfo point : points) {
                    if (point.isActive()) {
                        Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                                point.getLatitude(), point.getLongitude(), dist);

                        if (dist[0] < point.getRadius()) {

                            Intent i = new Intent(context, AlarmActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("point", point);
                            context.startActivity(i);
                        }
                    }
                }
            }
        }
    }

}
