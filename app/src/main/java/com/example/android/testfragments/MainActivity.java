package com.example.android.testfragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.testfragments.settings.SettingsActivity;
import com.example.android.testfragments.sync.DummyAccountService;
import com.example.android.testfragments.sync.SyncAdapter;


public class MainActivity extends AppCompatActivity implements
        MainActivityFragment.CallBack {


    private boolean mTwoPane = false;


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.myactivity);
        if (findViewById(R.id.weather_detail_container) != null) {
            mTwoPane = true;
            if (savedInstance != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailActivityFragment())
                        .commit();
            }
        }
        SyncAdapter.RequestSync(this);
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)
            startActivity(new Intent(this, SettingsActivity.class));
        return true;
    }

    public void onItemSelected(Uri uri) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable("URI",uri);

            DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
            detailActivityFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container,detailActivityFragment)
                    .commit();
        }
        else {
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.setData(uri);
            startActivity(detailIntent);
        }
    }

}
