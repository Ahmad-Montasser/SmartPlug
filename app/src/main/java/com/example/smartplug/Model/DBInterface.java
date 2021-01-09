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

import java.util.ArrayList;


public class DBInterface {
    private MyLocation location;
    private MyPlug plug;
    private FirebaseDatabase database;
    private DatabaseReference myLocationRef;
    private DatabaseReference myPlugRef;
    private MutableLiveData<MyLocation> locationLiveList;
    private MutableLiveData<MyPlug> plugLiveList;
    private final String TAG = "DBI ======";
    private ArrayList<MyPlug> pluglist;

    public DBInterface() {
        database = FirebaseDatabase.getInstance();
        myPlugRef = database.getReference("/Plugs");
        myLocationRef = database.getReference("/Locations");
        locationLiveList = new MutableLiveData<MyLocation>();
        plugLiveList = new MutableLiveData<MyPlug>();
        pluglist = new ArrayList<MyPlug>();
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
                        if (ds.hasChild("Location") && ds.hasChild("Status")) {
                            plug = new MyPlug(ds.getKey(), ds.child("Location").getValue().toString(),
                                    Integer.parseInt(ds.child("Status").getValue().toString()));
                            if (!pluglist.contains(plug))
                                pluglist.add(plug);
                            Log.d(TAG, plug.plugName);
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
        MyPlug p = new MyPlug(plugName, locationName, 0);
        myPlugRef.child(plugName).push();
        myPlugRef.child(plugName).child("Location").setValue(locationName);
        myPlugRef.child(plugName).child("Status").setValue(0);

    }

    public void addLocation(String locationName, double locationLatitude, double locationLongitude) {
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


    public void togglePlug(String plugName) {
        myPlugRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals(plugName)) {
                        if (ds.hasChild("Location") && ds.hasChild("Status")) {
                            MyPlug dummyPlug;
                            if (plug.getStatus() == 0) {
                                myPlugRef.child(plugName).child("Status").setValue(1);
                                dummyPlug = new MyPlug(ds.getKey(), ds.child("Location").getValue().toString(),
                                        1);
                            } else {
                                myPlugRef.child(plugName).child("Status").setValue(0);
                                dummyPlug = new MyPlug(ds.getKey(), ds.child("Location").getValue().toString(),
                                        0);
                            }
                            plugLiveList.setValue(dummyPlug);
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

    public void setPlugStatus(String locationName, int status) {
        DBFetch();
        Log.d(TAG, locationName + " " + status);
        Log.d(TAG, pluglist.size() + " size");
        for (MyPlug p : pluglist) {
            Log.d(TAG, p.getLocationName() + "dsds");

            if (p.getLocationName().equals(locationName)) {
                Log.d(TAG, p.getLocationName() + "aaaaa");
                myPlugRef.child(p.getPlugName()).child("Status").setValue(status);
            }
        }
    }
}
