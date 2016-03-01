package com.example.android.testfragments;

import android.content.Context;
import android.database.Cursor;

import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class MyCursorAdapter extends CursorAdapter {
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_TOMORROW = 1;



    public static final int _ID = 0;
    public static final int DATE = 1;
    public static final int WEATHER_ID = 2;
    public static final int SHORT_DESC = 3;
    public static final int MAX = 4;
    public static final int MIN = 5;
    public static final int WIND_SPEED= 6;
    public static final int DEGREE = 7;
    public static final int HUMIDITY = 8;
    public static final int PRESSURE= 9;
    public MyCursorAdapter(Context context, Cursor cursor,int flags) {
        super(context,cursor,flags);
    }
    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_TOMORROW;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId;
        if (viewType == VIEW_TYPE_TOMORROW) {
            layoutId = R.layout.listview_item_forecats;
        } else {
            layoutId = R.layout.list_item_forecast_today;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int weather_id = cursor.getInt(WEATHER_ID);

        viewHolder.iconView.setImageResource(Wizard.parseWeatherCode(weather_id, cursor.getPosition()));


        String day = cursor.getString(1);
        viewHolder.dateView.setText(day);


        String forecast = cursor.getString(3);
        viewHolder.forecastView.setText(forecast);


        double high = cursor.getDouble(4);
        String tmp = context.getString(R.string.format_temperature, high);
        viewHolder.highView.setText(tmp);


        double low = cursor.getDouble(5);
        tmp = context.getString(R.string.format_temperature,low);
        viewHolder.lowView.setText(tmp);
    }



    public  class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView forecastView;
        public final TextView highView;
        public final TextView lowView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            forecastView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowView = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }
}
