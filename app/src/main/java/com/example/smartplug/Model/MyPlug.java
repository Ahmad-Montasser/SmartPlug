package com.example.smartplug.Model;

public class MyPlug {
    MyLocation ml;
    String plugName;

    MyPlug(String name, MyLocation ml) {
        this.ml = ml;
        this.plugName = name;
    }

    public MyLocation getMl() {
        return ml;
    }

    public String getPlugName() {
        return plugName;
    }
}
