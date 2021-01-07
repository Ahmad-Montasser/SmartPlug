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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartplug.Model.CustomViewModel;
import com.example.smartplug.Model.MyLocation;
import com.example.smartplug.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AddLocationFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton addLocationButton;
    private final String TAG = "ADDLOCATIONFRAGMENT ======";
    public static ArrayList<MyLocation> locationList;
    Context context;
    CustomAdapter locationAdapter;
    ButtonListener buttonListener;
    CustomViewModel customViewModel;
    public AddLocationFragment() {
        super(R.layout.add_location_layout);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.locationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        addLocationButton = getView().findViewById(R.id.addLocationButton);
        buttonListener = new ButtonListener(this);
        addLocationButton.setOnClickListener(buttonListener);
        locationList = new ArrayList<MyLocation>();
        locationAdapter = new CustomAdapter(locationList, 0);
        recyclerView.setAdapter(locationAdapter);
        customViewModel = ViewModelProviders.of(this).get(CustomViewModel.class);
        customViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), new Observer<MyLocation>() {
            @Override
            public void onChanged(MyLocation myLocation) {
                if (!locationList.contains(myLocation))
                    locationList.add(myLocation);
                Log.d(TAG, myLocation.getName());
                locationAdapter.notifyItemRangeInserted(0, locationList.size());
                locationAdapter.notifyItemRangeRemoved(0, locationList.size());
            }

        });
    }




}