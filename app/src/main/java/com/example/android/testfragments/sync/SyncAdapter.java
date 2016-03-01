package com.example.android.testfragments.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.testfragments.R;
import com.example.android.testfragments.data.DBContract;
import com.example.android.testfragments.data.WeatherProvider;
import com.example.android.testfragments.Wizard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.URL;
import java.text.SimpleDateFormat;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = SyncAdapter.class.getSimpleName();

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SYNC_INTERVAL_IN_MINUTES = 180;
    private static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;

    long unixstamp;
    int weather_id;
    String mDayTime;
    String mCondition;
    double mWindSpeed;
    double mWindDegree;
    double mHumidity;
    double mHighTemp;
    double mLowTemp;
    double mPressure;

    private SimpleDateFormat mSimpleDateFormat;
    private SharedPreferences mPreferences;
    private String mProvider;
    private String mLocation;


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.i(TAG, "created");
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSimpleDateFormat = new SimpleDateFormat("EEE, d MMM");
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        mProvider = mPreferences.getString("weather_provider", "openweathermap");
        mLocation = mPreferences.getString("location", "Yakutsk");
        Log.d(TAG,"performSyc");
        SyncNow();
    }

    public static void RequestSync(Context context) {
        Account account = DummyAccountService.getAccount();
        AccountManager manager = (AccountManager)  context.getSystemService(Context.ACCOUNT_SERVICE);
        manager.addAccountExplicitly(account, null, null);
        Bundle settings = new Bundle();
        settings.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
        settings.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(account, "com.example.android.testfragments.app", settings);
        ContentResolver.setSyncAutomatically(account, "com.example.android.testfragments.app", true);
        ContentResolver.addPeriodicSync(account, "com.example.android.testfragments.app", Bundle.EMPTY, SYNC_INTERVAL);
    }
    public static void CancellSync(Context context) {

    }
    public  void SyncNow() {
        Log.i(TAG, "Sync performed");
        StringBuffer jsonStr = new StringBuffer("");
        BufferedReader buffer;
        String line;
        try {
            URL url = new URL(buildURL());
            Log.i(TAG, url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            buffer = new BufferedReader(new InputStreamReader(in));
            line = buffer.readLine();
            while (line != null) {
                jsonStr.append(line + "\n");
                line = buffer.readLine();
            }
        } catch (MalformedURLException e) {
            Log.d(TAG, "Invalid URL");
        } catch (IOException e) {
            Log.d(TAG, "IOEXCEPTION");
        } finally {

            getTemperatureForDay(jsonStr.toString());
            Log.i(TAG, "finally");
        }
    }

    public void getTemperatureForDay(String weatherStr) {
        if (mProvider.equals("openweathermap")) {
            parseOWM(weatherStr);
        } else {
            parseWeatherUnderground(weatherStr);
        }


    }

    public String buildURL() {

        if (mProvider.equals("openweathermap")) {
            String lang = getContext().getString(R.string.lang_OWM);
            Uri uri = Uri.parse("http://api.openweathermap.org/data/2.5/forecast/daily?")
                    .buildUpon()
                    .appendQueryParameter("q", mLocation)
                    .appendQueryParameter("mode", "json")
                    .appendQueryParameter("units", "metric")
                    .appendQueryParameter("cnt", "10")
                    .appendQueryParameter("appid", "0aad10e3b50976215c7df4ef133f40c0")
                    .appendQueryParameter("lang",lang)
                    .build();
            return uri.toString();
        } else {
            String lang = getContext().getString(R.string.lang_WU);
            String zmv = mPreferences.getString("zmv", "123");
            Uri uri = Uri.parse("http://api.wunderground.com/api/7c84b16bae4cd045/forecast10day/"+lang+"/q/")
                    .buildUpon()
                    .appendEncodedPath("zmw:" + zmv + ".json")
                    //.appendEncodedPath("autoip" + ".json")
                    .build();
            return uri.toString();
        }

    }

    public void parseOWM(String weatherStr) {
        try {
            JSONObject jsonWeather = new JSONObject(weatherStr);
            JSONArray dayList = jsonWeather.getJSONArray("list");
            if (dayList.length() > 0)
                getContext().getContentResolver().delete(WeatherProvider.CONTENT_URI, null, null);
            for (int i = 0; i < dayList.length(); i++) {
                JSONObject day = dayList.getJSONObject(i);
                unixstamp = day.getLong("dt") * 1000;
                mDayTime = mSimpleDateFormat.format(unixstamp);
                mCondition = day.getJSONArray("weather").getJSONObject(0).getString("description");
                mHighTemp = day.getJSONObject("temp").getDouble("max");
                mLowTemp = day.getJSONObject("temp").getDouble("min");
                weather_id = day.getJSONArray("weather").getJSONObject(0).getInt("id");
                mWindSpeed = day.getDouble("speed");
                mWindDegree = day.getDouble("deg");
                mHumidity = day.getInt("humidity");
                mPressure = day.getDouble("pressure");
                writeToDB();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void parseWeatherUnderground(String weatherStr) {
        try {
            JSONObject weatherUnderground = new JSONObject(weatherStr);
            JSONObject forecast = weatherUnderground.getJSONObject("forecast");
            JSONObject simpleForecast = forecast.getJSONObject("simpleforecast");
            JSONArray forecastDay = simpleForecast.getJSONArray("forecastday");
            if (forecastDay.length() > 0)
                getContext().getContentResolver().delete(WeatherProvider.CONTENT_URI, null, null);
            for (int i = 0; i < forecastDay.length(); i++) {
                JSONObject day = forecastDay.getJSONObject(i);

                unixstamp = day.getJSONObject("date").getLong("epoch") * 1000;
                mDayTime = mSimpleDateFormat.format(unixstamp);

                mHighTemp = day.getJSONObject("high").getInt("celsius");

                mLowTemp = day.getJSONObject("low").getInt("celsius");

                mCondition = day.getString("conditions");

                mWindSpeed = day.getJSONObject("avewind").getInt("kph");

                mWindDegree = day.getJSONObject("avewind").getInt("degrees");

                mHumidity = day.getInt("avehumidity");

                weather_id = Wizard.switchIconToWeatherID(day.getString("icon"));
                writeToDB();


            }

        } catch (JSONException e) {
            e.getMessage();
        }

    }

    public void writeToDB() {
        ContentValues cv = new ContentValues();
        cv.put(DBContract.WeatherEntry.COL_DATE, mDayTime);
        cv.put(DBContract.WeatherEntry.COL_WEATHER_ID, weather_id);
        cv.put(DBContract.WeatherEntry.COL_SHORT_DESC, mCondition);
        cv.put(DBContract.WeatherEntry.COL_MAX, mHighTemp);
        cv.put(DBContract.WeatherEntry.COL_MIN, mLowTemp);
        cv.put(DBContract.WeatherEntry.COL_WIND_SPEED, mWindSpeed);
        cv.put(DBContract.WeatherEntry.COL_DEGREE, mWindDegree);
        cv.put(DBContract.WeatherEntry.COL_HUMIDITY, mHumidity);
        cv.put(DBContract.WeatherEntry.COL_PRESSURE, mPressure);
        getContext().getContentResolver().insert(WeatherProvider.CONTENT_URI, cv);
    }
}

