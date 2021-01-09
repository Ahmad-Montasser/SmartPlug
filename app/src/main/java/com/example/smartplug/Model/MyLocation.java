package com.example.smartplug.Model;

import androidx.annotation.Nullable;

public class MyLocation {
    private String name;
    private double longitude, latitude;

    public MyLocation(String name, double latitude, double longitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        MyLocation object = (MyLocation) obj;
        if (object.getName() == this.name)
            return true;
        else
            return false;
    }
}