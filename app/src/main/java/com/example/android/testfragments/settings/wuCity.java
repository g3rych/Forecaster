package com.example.android.testfragments.settings;

public class wuCity {
    private String city_name;
    private String zmv;

    public wuCity(String city_name,String zmv) {
        this.city_name = city_name;
        this.zmv = zmv;
    }
    @Override
    public String toString() {
        return city_name;
    }

    public String getZmv() {
        return zmv;
    }
}
