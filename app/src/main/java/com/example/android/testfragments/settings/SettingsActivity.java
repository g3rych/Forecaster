package com.example.android.testfragments.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.testfragments.R;

import java.io.BufferedReader;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.CallBack {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
    }

    @Override
    public void onLocationChanged(String location) {
        Bundle bundle = new Bundle();
        bundle.putString("loc", location);
        LocationsFragment fragment = new LocationsFragment();
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(),"myDialog");
    }
}
