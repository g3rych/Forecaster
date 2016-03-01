package com.example.android.testfragments.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;

import com.example.android.testfragments.data.DBContract.WeatherEntry;

public class WeatherProvider extends ContentProvider {
    private static String TAG = WeatherProvider.class.getSimpleName();

    private static UriMatcher uriMatcher;
    public static final String URL = "content://"+DBContract.AUTHORITY+"/"+WeatherEntry.TABLE_NAME;
    public static final Uri CONTENT_URI = Uri.parse(URL);
    private static final int DAYS = 1;
    private static final int DAY = 2;

    SQLiteDatabase db;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DBContract.AUTHORITY, WeatherEntry.TABLE_NAME,DAYS);
        uriMatcher.addURI(DBContract.AUTHORITY, WeatherEntry.TABLE_NAME + "/#", DAY);
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        WeatherDBHelper weatherDBHelper = new WeatherDBHelper(context);
        db = weatherDBHelper.getWritableDatabase();

        return (db == null) ? true : false;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            case DAYS :
                retCursor = db.query(WeatherEntry.TABLE_NAME,projection,selection,null,null,null,null);
                break;
            case DAY :
                retCursor = db.query(
                        WeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            default: throw new UnsupportedOperationException("Unknown URI" +uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case DAYS : return ContentResolver.CURSOR_DIR_BASE_TYPE+"vnd.example.DAYS";
            case DAY : return  ContentResolver.CURSOR_ITEM_BASE_TYPE+"vnd.example.DAY";
            default: throw new IllegalArgumentException("Unknown URI");
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Long id = db.insert(WeatherEntry.TABLE_NAME,null,values);
        if (id > 0) {
            getContext().getContentResolver().notifyChange(uri,null);
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        count = db.delete(WeatherEntry.TABLE_NAME,null,null);
        getContext().getContentResolver().notifyChange(uri,null);
        Log.d(TAG,count+"");
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
