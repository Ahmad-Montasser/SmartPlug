package com.example.smartplug.UI;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartplug.Model.MyPlug;
import com.example.smartplug.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PlugFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton addPlugButton;
    ArrayList<MyPlug> plugList;
    Context context;
    CustomAdapter plugAdapter;

    FirebaseDatabase database;
    DatabaseReference myRef;
    ButtonListener buttonListener;

    public PlugFragment() {
        super(R.layout.plugs_layout);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.plugRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        init();
    }

    void init() {
        context = getActivity();
        database = FirebaseDatabase.getInstance();
        plugList = new ArrayList<MyPlug>();
        buttonListener = new ButtonListener(context);
        myRef = database.getReference("/Plugs");
        addPlugButton = getView().findViewById(R.id.addPlugButton);
        addPlugButton.setOnClickListener(buttonListener);
    }


}
