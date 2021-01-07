package com.example.smartplug.Model;

public class MyPlug {
    String locationName;
    String plugName;

    MyPlug(String name, String ml) {
        this.locationName = ml;
        this.plugName = name;
    }

    public String getMl() {
        return locationName;
    }

    public String getPlugName() {
        return plugName;
    }
}
