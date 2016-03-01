package com.example.android.testfragments.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.android.testfragments.R;
import com.example.android.testfragments.sync.SyncAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LocationsFragment extends DialogFragment {
    private String loc;
    private ArrayAdapter<wuCity> adapter;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            loc = args.getString("loc");
        }

        adapter = new ArrayAdapter<wuCity>(getActivity(),
                R.layout.list_item_text_view,
                R.id.list_item_settings);

        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Выберети местоположение");
        dialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("zmv", adapter.getItem(which).getZmv());
                editor.putString("location",adapter.getItem(which).toString());
                editor.apply();

                Toast.makeText(getActivity(), adapter.getItem(which).getZmv(),Toast.LENGTH_SHORT).show();

            }
        });
        new FetchLocationsTask().execute(loc);
        return dialogBuilder.create();

   }

    public class FetchLocationsTask extends AsyncTask<String, Void, Void> {
        private ArrayList<wuCity> cityList = new ArrayList<>();


        @Override
        protected Void doInBackground(String... params) {
            StringBuilder builder = new StringBuilder();
            try {
                Uri uri = Uri.parse("http://autocomplete.wunderground.com/aq")
                        .buildUpon()
                        .appendQueryParameter("query", params[0])
                        .build();
                URL url = new URL(uri.toString());
                Log.i("TAG", uri.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept-Language", "ru-RU");
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("TAG", builder.toString());
            getLocations(builder.toString());
            return null;
        }

        public void getLocations(String s) {

            try {
                JSONObject locations = new JSONObject(s);
                JSONArray results = locations.getJSONArray("RESULTS");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject item = results.getJSONObject(i);
                    String city = item.getString("name");
                    String zmv = item.getString("zmw");
                    cityList.add(new wuCity(city,zmv));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            wuCity[] items = cityList.toArray(new wuCity[0]);
            adapter.addAll(items);
        }
    }
}
