package com.example.android.testfragments;

import com.example.android.testfragments.R;

public class Wizard {

    public static int switchIconToWeatherID(String icon) {
        switch(icon) {
            case "chancerain" : return 500;
            case "chancesnow" : return 600;
            case "clear" : return 800;
            case "cloudy" : return 801;
            case "tstorms" : return 200;
            case "nt_partlycloudy" :return 801;
            case "nt_chancerain" : return 500;
            case "rain" : return 500;
            case "partlycloudy" : return 801;
            case "snow" : return 600;
            case "nt_snow" : return 600;
            default: return 800;
        }
    }

    public static int parseWeatherCode(int weather_code,int c) {
        int retResource;
        if (weather_code >= 801 && weather_code <= 804 && c == 0) {
            return R.drawable.art_clouds;
        } else
        if (weather_code >= 801 && weather_code <= 804) {
            return R.drawable.ic_cloudy;
        }
        weather_code = weather_code  / 100;
        switch (weather_code) {
            case 6 : retResource = (c == 0) ? R.drawable.art_snow : R.drawable.ic_snow;
                break;
            case 8 : retResource = (c == 0) ? R.drawable.art_clear : R.drawable.ic_clear;
                break;
            case 5 : retResource = (c == 0) ? R.drawable.art_rain : R.drawable.ic_rain;
                break;
            default: retResource = R.drawable.ic_clear;
                break;
        }
        return retResource;
    }
}
