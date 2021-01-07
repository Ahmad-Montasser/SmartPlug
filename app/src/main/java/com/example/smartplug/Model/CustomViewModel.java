package com.example.smartplug.Model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CustomViewModel extends AndroidViewModel {
    private DBInterface DBI;
    private MutableLiveData<MyLocation> locationLiveData;
    private MutableLiveData<MyPlug> plugLiveData;

    public CustomViewModel(@NonNull Application application) {
        super(application);
        DBI = new DBInterface();
        locationLiveData = DBI.getLocationLiveList();
        plugLiveData = DBI.getPlugLiveList();
    }

    public MutableLiveData<MyLocation> getLocationLiveData() {
        return locationLiveData;
    }

    public MutableLiveData<MyPlug> getPlugLiveData() {
        return plugLiveData;
    }

    public void addLocation(String locationName) {
        DBI.addLocation(locationName);
    }

    public void addPlug(String plugName, String locationName) {
        DBI.addPlug(plugName, locationName);
    }
}
