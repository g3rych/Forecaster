package com.example.android.testfragments.data;


import android.provider.BaseColumns;

public class DBContract {
    public static final String AUTHORITY = "com.example.android.testfragments.app";

    public static final class WeatherEntry implements BaseColumns {
        public static final String TABLE_NAME = "WeatherEntry";
        public static final String COL_WEATHER_ID = "weather_id";
        public static final String COL_DATE = "date";
        public static final String COL_SHORT_DESC = "short_description";
        public static final String COL_HUMIDITY = "humidity";
        public static final String COL_MAX = "max";
        public static final String COL_MIN = "min";
        public static final String COL_PRESSURE = "pressure";
        public static final String COL_WIND_SPEED = "wind_speed";
        public static final String COL_DEGREE = "degree";

    }
}
