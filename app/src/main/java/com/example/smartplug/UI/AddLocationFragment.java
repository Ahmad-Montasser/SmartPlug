package com.example.smartplug.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartplug.ViewModel.CustomViewModel;
import com.example.smartplug.Model.MyLocation;
import com.example.smartplug.Model.Permissions;
import com.example.smartplug.R;
import com.google.android.gms.location.LocationResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;


public class AddLocationFragment extends Fragment {
    int FINE_LOCATION_PERMISSION_REQUEST_CODE = 99;
    int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 100;
    int COARSE_LOCATION_PERMISSION_REQUEST_CODE = 101;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Boolean NeedToShowRequestPermissionRationale = true;
    private int debug = 0;
    private RecyclerView recyclerView;
    private FloatingActionButton addLocationButton;
    private final String TAG = "ADDLOCATIONFRAGMENT ======";
    private ArrayList<MyLocation> locationList;
    private Context context;
    private CustomAdapter locationAdapter;
    private ButtonListener buttonListener;
    private CustomViewModel customViewModel;

    public AddLocationFragment() {
        super(R.layout.add_location_layout);
        context = getContext();
    }

    protected void createLocationRequest() {
        Log.d(TAG, "createLocationRequest");
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d(TAG, "LocationCallback");

                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d("location", "Location received");
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(locationRequest, locationCallback, null);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.locationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        addLocationButton = getView().findViewById(R.id.addLocationButton);
        buttonListener = new ButtonListener(this, getViewLifecycleOwner());
        addLocationButton.setOnClickListener(buttonListener);
        locationList = new ArrayList<MyLocation>();
        locationAdapter = new CustomAdapter(locationList, 0);
        buttonListener = new ButtonListener(this, getViewLifecycleOwner());
        recyclerView.setAdapter(locationAdapter);
        createLocationRequest();
        customViewModel = ViewModelProviders.of(this).get(CustomViewModel.class);
        customViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), new Observer<MyLocation>() {
            @Override
            public void onChanged(MyLocation myLocation) {

                if (!locationList.contains(myLocation))
                    locationList.add(myLocation);
                locationAdapter.notifyItemRangeInserted(0, locationList.size());
                locationAdapter.notifyItemRangeRemoved(0, locationList.size());
            }

        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Permissions.isAccessCoarseLocationGranted(getContext())) {
            Log.d(TAG, "dfsfsdfsdfdsddf");
            if (Permissions.isLocationEnabled(getActivity())) {
                setUpLocationListener();
            } else {
                Permissions.showGPSNotEnabledDialog(getActivity());
            }
        } else {
            Permissions.requestAccessFineLocationPermission(getActivity(), FINE_LOCATION_PERMISSION_REQUEST_CODE);
            Permissions.requestAccessBackgroundLocationPermission(getActivity(), BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE);
            Permissions.requestAccessCoarseLocationPermission(getActivity(), COARSE_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    private void setUpLocationListener() {
        Log.d(TAG, "setUpLocationListener");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                    Log.d("location", "Location received" + location.getLatitude() + " " + location.getLongitude());
            }
        });
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINE_LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (Permissions.isLocationEnabled(context))
                    setUpLocationListener();
                else
                    Permissions.showGPSNotEnabledDialog(context);
            } else if (requestCode == BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE) {

            } else {
                if (NeedToShowRequestPermissionRationale) {
                    Permissions.ShowRequestPermissionRationale(context, getActivity(), FINE_LOCATION_PERMISSION_REQUEST_CODE);
                    NeedToShowRequestPermissionRationale = false;
                } else {

                    Toast.makeText(context, "Permission Not Granted", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}