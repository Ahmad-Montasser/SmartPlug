package com.example.smartplug.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartplug.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AddLocation extends Fragment implements LocationListener {
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView recyclerView;
    double locationLatitude = 2, locationLongitude = 2;
    LinearLayout linearLayout;
    String locationName;
    FloatingActionButton addLocationButton;
    LocationManager locationManager;
    ArrayList<MyLocation> locationList;
    Context context;
    CustomAdapter locationAdapter;

    public AddLocation() {
        super(R.layout.add_location_layout);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.locationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        initialize();
    }

    private void initialize() {
        context = getActivity();
        database = FirebaseDatabase.getInstance();
        locationList = new ArrayList<MyLocation>();
        myRef = database.getReference("/Locations");
        addLocationButton = getView().findViewById(R.id.addLocationButton);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locationList = new ArrayList<MyLocation>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey() != null) {
                        if (ds.hasChild("Latitude") && ds.hasChild("Longitude")) {
                            MyLocation l = new MyLocation(ds.getKey().toString(), Double.parseDouble(ds.child("Latitude").getValue().toString()), Double.parseDouble(ds.child("Longitude").getValue().toString()));
                            locationList.add(l);
                            locationAdapter = new CustomAdapter(locationList);
                            recyclerView.setAdapter(locationAdapter);
                            locationAdapter.notifyItemRangeInserted(0, locationList.size());
                            locationAdapter.notifyItemRangeRemoved(0, locationList.size());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogueBuilder = new AlertDialog.Builder(getActivity());
                alertDialogueBuilder.setTitle("Pls Enter The Current Location Name");
                // Set up the input
                final EditText input = new EditText(getActivity());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                alertDialogueBuilder.setView(input);

                // Set up the buttons
                alertDialogueBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationName = input.getText().toString();
                        myRef.child(locationName).push();
                        myRef.child(locationName).child("Latitude").setValue(locationLatitude);
                        myRef.child(locationName).child("Longitude").setValue(locationLongitude);

                    }
                });
                alertDialogueBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialogueBuilder.show();
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        locationLatitude = location.getLatitude();
        locationLongitude = location.getLongitude();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    class MyLocation {
        private String name;
        private double longitude, latitude;

        public String getName() {
            return name;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        MyLocation(String name, double longitude, double latitude) {
            this.name = name;
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }

}