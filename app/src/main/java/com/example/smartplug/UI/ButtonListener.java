package com.example.smartplug.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.smartplug.Model.MyLocation;
import com.example.smartplug.ViewModel.CustomViewModel;
import com.example.smartplug.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class ButtonListener implements View.OnClickListener {
    double locationLatitude = 2, locationLongitude = 2;
    String locationName, plugName, plugLocationName;
    Context context;
    CustomViewModel customViewModel;
    Fragment f;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<MyLocation> locationList;
    private final String TAG = "ButtonListener ======";

    ButtonListener(Fragment f, LifecycleOwner LO) {
        context = f.getContext();
        this.f = f;
        customViewModel = ViewModelProviders.of(f).get(CustomViewModel.class);
        arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice);
        locationList = new ArrayList<MyLocation>();
        customViewModel.getLocationLiveData().observe(LO, new Observer<MyLocation>() {
            @Override
            public void onChanged(MyLocation myLocation) {
                if (!locationList.contains(myLocation))
                    locationList.add(myLocation);
            }

        });
    }

    public void updateLocationList() {
        for (MyLocation m : locationList) {
            arrayAdapter.add(m.getName());
            Log.d(TAG, m.getName());
        }
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder alertDialogueBuilder = new AlertDialog.Builder(context);
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogueBuilder.setView(input);
        customViewModel = ViewModelProviders.of(f).get(CustomViewModel.class);
        switch (view.getId()) {
            case R.id.addLocationButton:
                alertDialogueBuilder.setTitle("Enter the Location Name");
                alertDialogueBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationName = input.getText().toString();
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        LocationServices.getFusedLocationProviderClient(context).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                locationLatitude = location.getLatitude();
                                locationLongitude = location.getLongitude();
                                customViewModel.addLocation(locationName, locationLatitude, locationLongitude);


                            }
                        });
                    }
                });
                alertDialogueBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialogueBuilder.show();
                break;
            case R.id.addPlugButton:

                alertDialogueBuilder.setTitle("Enter the Plug Name");
                alertDialogueBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        plugName = input.getText().toString();
                        if (plugName == null || plugName.trim().equals("")) {
                            Toast.makeText(context, "You must enter a valid name", Toast.LENGTH_LONG).show();
                            return;
                        } else if (plugLocationName == null) {
                            Toast.makeText(context, "You must choose a location", Toast.LENGTH_LONG).show();
                            return;
                        } else
                            customViewModel.addPlug(plugName, plugLocationName);

                    }
                });
                alertDialogueBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialogueBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialogueBuilder.show();
                updateLocationList();
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                builderSingle.setTitle("Select the plug location:-");
                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                plugLocationName = strName;
                                arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice);
                            }
                        });
                        builderInner.show();
                    }
                });

                builderSingle.show();
                break;
        }
    }

}
