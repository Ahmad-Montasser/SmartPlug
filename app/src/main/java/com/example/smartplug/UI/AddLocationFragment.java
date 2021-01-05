package com.example.smartplug.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartplug.Model.MyLocation;
import com.example.smartplug.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class AddLocationFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView recyclerView;
    double locationLatitude = 2, locationLongitude = 2;
    String locationName;
    FloatingActionButton addLocationButton;
    public static ArrayList<MyLocation> locationList;
    Context context;
    CustomAdapter locationAdapter;
    ButtonListener buttonListener;

    public AddLocationFragment() {
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
        buttonListener = new ButtonListener(context);
        myRef = database.getReference("/Locations");
        addLocationButton = getView().findViewById(R.id.addLocationButton);
        addLocationButton.setOnClickListener(buttonListener);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locationList = new ArrayList<MyLocation>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey() != null) {
                        if (ds.hasChild("Latitude") && ds.hasChild("Longitude")) {
                            MyLocation l = new MyLocation(ds.getKey().toString(), Double.parseDouble(ds.child("Latitude").getValue().toString()), Double.parseDouble(ds.child("Longitude").getValue().toString()));
                            locationList.add(l);
                            locationAdapter = new CustomAdapter(locationList, 0);
                            recyclerView.setAdapter(locationAdapter);
                            locationAdapter.notifyItemRangeInserted(0, locationList.size());
                            locationAdapter.notifyItemRangeRemoved(0, locationList.size());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database Error ======", error.getMessage());
                Toast.makeText(context, "Failed to update Database", Toast.LENGTH_LONG).show();
            }
        });
    }


}