package com.example.smartplug.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    final String TAG = "GeofenceBroadcastReceiver =====";
    private DBInterface DBI = new DBInterface();

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            Log.d(TAG, "enter exit");

            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                for (Geofence g : triggeringGeofences) {
                    DBI.setPlugStatus(g.getRequestId(), 1);
                    Log.d(TAG, g.getRequestId() + "  1");
                    Log.d(TAG, "enter");

                }
            } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                for (Geofence g : triggeringGeofences) {
                    DBI.setPlugStatus(g.getRequestId(), 0);
                    Log.d(TAG, g.getRequestId() + "  0");
                    Log.d(TAG, "exit");

                }
            }
        } else {
            // Log the error.
            Log.d(TAG, "error");
        }
    }
}
