package com.example.android.testfragments.settings;


import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;


import com.example.android.testfragments.R;
import com.example.android.testfragments.sync.SyncAdapter;


public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener
 {
    private EditTextPreference mLocationPreference;
    private SharedPreferences mPreferences;
    private ListPreference mProviderPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mLocationPreference = (EditTextPreference) findPreference("location");
        mLocationPreference.setOnPreferenceChangeListener(this);

        mProviderPreference = (ListPreference) findPreference("weather_provider");
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mPreferences.registerOnSharedPreferenceChangeListener(this);

        updateSummarys();


    }

     @Override
     public boolean onPreferenceChange(Preference preference, Object newValue) {
         Log.d("Settings","preference change " +newValue.toString());


         ((CallBack) getActivity()).onLocationChanged(newValue.toString());
         updateSummarys();
         return true;
     }

     @Override
     public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
         Log.d("Settings","shared change " +key);
         updateSummarys();
     }

     public void updateSummarys() {
         String location = mPreferences.getString("location", "");
         String weatherProvider = mPreferences.getString("weather_provider","");
         mLocationPreference.setSummary(location);
         mProviderPreference.setSummary(weatherProvider);
     }




    interface CallBack {
        public void onLocationChanged(String location);

    }
}
