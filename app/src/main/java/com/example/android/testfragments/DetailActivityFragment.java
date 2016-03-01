package com.example.android.testfragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private Uri message;



    TextView dateView;
    TextView highTempView;
    TextView lowTempView;
    TextView humidityView;
    TextView windView;
    TextView pressureView;
    TextView forecastView;
    ImageView weather_icon;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.detail_fragment, container, false);
            dateView = (TextView) rootView.findViewById(R.id.date_view);
            highTempView = (TextView) rootView.findViewById(R.id.hight_temp_view);
            lowTempView = (TextView) rootView.findViewById(R.id.low_temp_view);
            humidityView = (TextView) rootView.findViewById(R.id.humidity_view);
            pressureView = (TextView) rootView.findViewById(R.id.pressure_view);
            forecastView = (TextView) rootView.findViewById(R.id.forecast_view);
            windView = (TextView) rootView.findViewById(R.id.wind_view);
            weather_icon = (ImageView) rootView.findViewById((R.id.weather_icon));

            getLoaderManager().initLoader(1,null,this);
            Bundle arguments = getArguments();
            if (arguments != null ) {
               message =  arguments.getParcelable("URI");
            }
            return  rootView;
        }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (message != null){
        String condition = message.getLastPathSegment();

        return new CursorLoader(getActivity(),message,null,"_id = ?",new String[] {condition},null);}
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            dateView.setText(data.getString(MyCursorAdapter.DATE));


            highTempView.setText(getString(R.string.format_temperature,
                    data.getDouble(MyCursorAdapter.MAX)));

            lowTempView.setText(getString(R.string.format_temperature,
                    data.getDouble(MyCursorAdapter.MIN)));

            forecastView.setText(data.getString(MyCursorAdapter.SHORT_DESC));

            humidityView.setText(getString(R.string.humidity_view,
                    data.getInt(MyCursorAdapter.HUMIDITY)));

            pressureView.setText(getString(R.string.pressure_view,
                    data.getDouble(MyCursorAdapter.PRESSURE)));





            windView.setText(getString(R.string.wind_view,
                    data.getDouble(MyCursorAdapter.WIND_SPEED)));
            weather_icon.setImageResource(Wizard.parseWeatherCode(data.getInt(MyCursorAdapter.WEATHER_ID), 0));



        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}

