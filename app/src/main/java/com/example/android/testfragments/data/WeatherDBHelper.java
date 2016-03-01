package com.example.android.testfragments.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.testfragments.data.DBContract.WeatherEntry;

public class WeatherDBHelper extends SQLiteOpenHelper {
    private static final String TAG = WeatherDBHelper.class.getSimpleName();
    static final String DB_NAME = "weather.db";
    static final int DB_VERSION = 1;

    public WeatherDBHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"database need to upgrade");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_DATABASE = "CREATE TABLE " +WeatherEntry.TABLE_NAME + " ( " +
                WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WeatherEntry.COL_DATE + " TEXT NOT NULL, " +
                WeatherEntry.COL_WEATHER_ID + " INTEGER NOT NULL, " +
                WeatherEntry.COL_SHORT_DESC + " TEXT NOT NULL, " +
                WeatherEntry.COL_MAX + " REAL NOT NULL, " +
                WeatherEntry.COL_MIN + " REAL NOT NULL, " +
                WeatherEntry.COL_WIND_SPEED + " REAL NOT NULL, " +
                WeatherEntry.COL_DEGREE + " REAL NOT NULL, " +
                WeatherEntry.COL_HUMIDITY + " INTEGER NOT NULL, " +
                WeatherEntry.COL_PRESSURE + " REAL NOT NULL " + " ); ";
        Log.d(TAG,CREATE_DATABASE);
        db.execSQL(CREATE_DATABASE);
    }
}
