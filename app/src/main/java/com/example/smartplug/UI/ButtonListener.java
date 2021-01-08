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
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.smartplug.ViewModel.CustomViewModel;
import com.example.smartplug.R;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class ButtonListener implements View.OnClickListener {
    double locationLatitude = 2, locationLongitude = 2;
    String locationName, plugName;
    Context context;
    CustomViewModel customViewModel;
    Fragment f;
    private final String TAG = "ButtonListener ======";

    ButtonListener(Fragment f) {
        context = f.getContext();
        this.f = f;

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
                        customViewModel.addPlug(plugName, "sds");
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
        }
    }

}
