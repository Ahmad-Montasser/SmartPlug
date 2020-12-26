package com.example.smartplug.UI;

import androidx.fragment.app.Fragment;

import com.example.smartplug.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Plugs extends Fragment {
    final DatabaseReference myRef;
    FirebaseDatabase database;
    public Plugs(){
    super(R.layout.activity_main);
    database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Plugs/");

}
}
