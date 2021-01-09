package com.example.smartplug.Model;

import androidx.annotation.Nullable;

public class MyPlug {
    String locationName;
    String plugName;

    int Status;

    MyPlug(String name, String ml, int Status) {
        this.locationName = ml;
        this.plugName = name;
        this.Status = Status;
    }

    public int getStatus() {
        return Status;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getPlugName() {
        return plugName;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        MyPlug object = (MyPlug) obj;
        if (object.getPlugName().equals(this.plugName))
            return true;
        else
            return false;
    }
}
