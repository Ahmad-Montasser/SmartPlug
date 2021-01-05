package com.example.smartplug.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.smartplug.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ButtonListener implements View.OnClickListener {
    double locationLatitude = 2, locationLongitude = 2;
    String locationName, plugName;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Context context;

    ButtonListener(Context c) {
        context = c;
    }

    @Override
    public void onClick(View view) {
        database = FirebaseDatabase.getInstance();
        AlertDialog.Builder alertDialogueBuilder = new AlertDialog.Builder(context);
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogueBuilder.setView(input);
        switch (view.getId()) {
            case R.id.addLocationButton:
                myRef = database.getReference("/Locations");
                alertDialogueBuilder.setTitle("Enter the Location Name");
                // Set up the input
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

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
                break;
            case R.id.addPlugButton:
                myRef = database.getReference("/Plugs");
                alertDialogueBuilder.setTitle("Enter the Plug Name");
                // Set up the input
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                // Set up the buttons
                alertDialogueBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        plugName = input.getText().toString();

                    }
                });
                alertDialogueBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialogueBuilder.show();
                myRef.child(plugName).push();
                break;
        }
    }

}
