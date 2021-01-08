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

import com.example.smartplug.ViewModel.CustomViewModel;
import com.example.smartplug.Model.MyPlug;
import com.example.smartplug.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlugFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton addPlugButton;
    ArrayList<MyPlug> plugList;
    Context context;
    CustomAdapter plugAdapter;
    ButtonListener buttonListener;
    private final String TAG = "PLUGFRAGMENT ======";
    CustomViewModel customViewModel;

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
        buttonListener = new ButtonListener(this);
        addPlugButton.setOnClickListener(buttonListener);
        plugList = new ArrayList<MyPlug>();
        plugAdapter = new CustomAdapter(plugList, 1);
        recyclerView.setAdapter(plugAdapter);
        customViewModel = ViewModelProviders.of(this).get(CustomViewModel.class);
        customViewModel.getPlugLiveData().observe(getViewLifecycleOwner(), new Observer<MyPlug>() {
            @Override
            public void onChanged(MyPlug myPlug) {
                if (!plugList.contains(myPlug))
                    plugList.add(myPlug);
                plugAdapter.notifyItemRangeInserted(0, plugList.size());
                plugAdapter.notifyItemRangeRemoved(0, plugList.size());
            }
        });
    }



}
