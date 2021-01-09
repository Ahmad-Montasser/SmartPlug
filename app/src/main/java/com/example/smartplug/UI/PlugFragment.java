package com.example.smartplug.UI;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartplug.Model.Permissions;
import com.example.smartplug.ViewModel.CustomViewModel;
import com.example.smartplug.Model.MyPlug;
import com.example.smartplug.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlugFragment extends Fragment {
    int FINE_LOCATION_PERMISSION_REQUEST_CODE = 99;
    int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 100;
    int COARSE_LOCATION_PERMISSION_REQUEST_CODE = 101;

    RecyclerView recyclerView;
    FloatingActionButton addPlugButton;
    ArrayList<MyPlug> plugList;
    Context context;
    CustomAdapter plugAdapter;
    ButtonListener buttonListener;
    private final String TAG = "PLUGFRAGMENT ======";
    CustomViewModel customViewModel;
    ArrayList<MyPlug> dummy = new ArrayList<MyPlug>();

    public PlugFragment() {
        super(R.layout.plugs_layout);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.plugRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView = getView().findViewById(R.id.plugRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        addPlugButton = getView().findViewById(R.id.addPlugButton);
        buttonListener = new ButtonListener(this, getViewLifecycleOwner());
        addPlugButton.setOnClickListener(buttonListener);
        plugList = new ArrayList<MyPlug>();
        plugAdapter = new CustomAdapter(plugList, 1);
        recyclerView.setAdapter(plugAdapter);
        customViewModel = ViewModelProviders.of(this).get(CustomViewModel.class);
        customViewModel.getPlugLiveData().observe(getViewLifecycleOwner(), new Observer<MyPlug>() {
            @Override
            public void onChanged(MyPlug myPlug) {
                if (!plugList.contains(myPlug)) {
                    plugList.add(myPlug);
                } else {
                    ArrayList<MyPlug> toRemove = new ArrayList<>();
                    ArrayList<MyPlug> toAdd = new ArrayList<>();

                    for (MyPlug p : plugList) {

                        if (p.getStatus() != myPlug.getStatus() && p.equals(myPlug)) {
                            Log.d(TAG, p.getPlugName() + " " + myPlug.getPlugName() + "  " + p.getStatus() + "  " + myPlug.getStatus());
                            toRemove.add(p);
                            toAdd.add(myPlug);
                        }
                    }
                    plugList.removeAll(toRemove);
                    plugList.addAll(toAdd);
                }
                plugAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!(Permissions.isAccessCoarseLocationGranted(getContext()) && Permissions.isAccessFineLocationGranted(getContext()))) {
            Permissions.requestAccessFineLocationPermission(getActivity(), FINE_LOCATION_PERMISSION_REQUEST_CODE);
            Permissions.requestAccessBackgroundLocationPermission(getActivity(), BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE);
            Permissions.requestAccessCoarseLocationPermission(getActivity(), COARSE_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

}
