package com.apps.geo.notes.geo;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

public class LocationTracking {

    private static PendingIntent pendingIntent;

    private static PendingIntent getPendingIntent(Context context) {
        if (pendingIntent == null) {
            Intent intent = new Intent(context, LocationReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return pendingIntent;
    }

    public static void startLocationTracking(Context context) {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                getPendingIntent(context));
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                getPendingIntent(context));
    }

    public static void stopLocationTracking(Context context) {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(getPendingIntent(context));
    }

}
