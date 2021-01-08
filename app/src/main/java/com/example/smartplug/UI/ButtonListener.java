package com.example.smartplug.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.smartplug.ViewModel.CustomViewModel;
import com.example.smartplug.R;

public class ButtonListener implements View.OnClickListener {
    double locationLatitude = 2, locationLongitude = 2;
    String locationName, plugName;
    Context context;
    CustomViewModel customViewModel;
    Fragment f;

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
                        customViewModel.addLocation(locationName);
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
