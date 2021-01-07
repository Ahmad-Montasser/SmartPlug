package com.example.smartplug.Model;

import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DBInterface implements LocationListener {
    double locationLatitude = 2, locationLongitude = 2;
    private MyLocation location;
    private MyPlug plug;
    private FirebaseDatabase database;
    private DatabaseReference myLocationRef;
    private DatabaseReference myPlugRef;
    private MutableLiveData<MyLocation> locationLiveList;
    private MutableLiveData<MyPlug> plugLiveList;

    public DBInterface() {
        database = FirebaseDatabase.getInstance();
        myPlugRef = database.getReference("/Plugs");
        myLocationRef = database.getReference("/Locations");
        locationLiveList = new MutableLiveData<MyLocation>();
        plugLiveList = new MutableLiveData<MyPlug>();
        DBFetch();
    }

    private void DBFetch() {

        myLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey() != null) {
                        if (ds.hasChild("Latitude") && ds.hasChild("Longitude")) {
                            location = new MyLocation(ds.getKey(), Double.parseDouble(ds.child("Latitude").getValue().toString()),
                                    Double.parseDouble(ds.child("Longitude").getValue().toString()));
                            locationLiveList.setValue(location);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Location Error Location ======", error.getMessage());
            }
        });

        myPlugRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey() != null) {
                        if (ds.hasChild("Location")) {
                            plug = new MyPlug(ds.getKey(), ds.child("Location").getValue().toString());
                            plugLiveList.setValue(plug);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Plug Error Plug ======", error.getMessage());

            }
        });
    }

    public void addPlug(String plugName, String locationName) {
        myPlugRef.child(plugName).push();
        myPlugRef.child(plugName).child("Location").setValue(locationName);
    }

    public void addLocation(String locationName) {
        myLocationRef.child(locationName).push();
        myLocationRef.child(locationName).child("Latitude").setValue(locationLatitude);
        myLocationRef.child(locationName).child("Longitude").setValue(locationLongitude);
    }

    public MutableLiveData<MyLocation> getLocationLiveList() {
        return locationLiveList;
    }

    public MutableLiveData<MyPlug> getPlugLiveList() {
        return plugLiveList;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
