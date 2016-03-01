package com.example.android.testfragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.testfragments.data.WeatherProvider;




public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView listView;
    int mPosition = ListView.INVALID_POSITION;
    MyCursorAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstance) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.forecast_lisview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                long _id = cursor.getLong(0);

                Uri uri = WeatherProvider.CONTENT_URI.buildUpon().appendPath(Long.toString(_id)).build();

                ((CallBack) getActivity()).onItemSelected(uri);
                mPosition = position;

            }
        });
        adapter = new MyCursorAdapter(getContext(),null,0);
        View emptyView = rootView.findViewById(R.id.empty_weather_list);
        listView.setEmptyView(emptyView);
        listView.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

        if (savedInstance != null && savedInstance.containsKey("pos")) {
            mPosition = savedInstance.getInt("pos");
        }
//        Intent intent = new Intent(getActivity(), SunshineService.AlarmReceiver.class);
//       // getActivity().startService(intent);
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
//        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                5000,
//                5000,
//                alarmIntent);

        return rootView;
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
     adapter.swapCursor(cursor);
        if (mPosition != ListView.INVALID_POSITION) {
            Log.d("Sunshine",mPosition+"");
            listView.smoothScrollToPosition(mPosition);
        }
        updateEmptyList();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(getActivity(), WeatherProvider.CONTENT_URI,null,null,null,null);
        return loader;
    }

    public interface CallBack {
        public void onItemSelected(Uri uri);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("pos", mPosition);
        super.onSaveInstanceState(outState);

    }

    public void updateEmptyList() {
        TextView tv = (TextView) getActivity().findViewById(R.id.empty_weather_list);
        if (adapter.getCount() == 0) {
            if (Wizard.isConnected(getActivity())) {
                tv.setText(R.string.empty_list_network_unavailable);
            }
        }

    }

}
