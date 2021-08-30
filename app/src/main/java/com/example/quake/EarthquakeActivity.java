package com.example.quake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;


public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<earthquake>> {
    /** Adapter for the list of earthquakes. We create it global because of this
     * we no need to create everytime new object, just change the data set   */
    private EarthquakesAdapter mAdapter;
    ProgressBar loaderIndicator;
    private static final String USED_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=4&limit=56";
    TextView earthQuakeEmpty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Idea of Loaders", "onCreate()");
        earthQuakeEmpty =findViewById(R.id.noEarthquake);
        loaderIndicator =findViewById(R.id.indeterminateBar);
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected){
            loaderIndicator.setVisibility(View.GONE);
            earthQuakeEmpty.setVisibility(View.VISIBLE);
            earthQuakeEmpty.setText(R.string.no_internet_connection);
        }
        else{
            Log.i("Idea of Loaders", "initLoader()");
            getSupportLoaderManager().initLoader(1, null, this);

            // Find a reference to the {@link ListView} in the layout
            ListView earthquakeListView = findViewById(R.id.list);
            earthquakeListView.setEmptyView(earthQuakeEmpty);
            // Create a new {@link ArrayAdapter} of earthquakes
            mAdapter = new EarthquakesAdapter(EarthquakeActivity.this, new ArrayList<earthquake>());
            earthquakeListView.setAdapter(mAdapter);
            earthquakeListView.setOnItemClickListener((parent, view, position, id) -> {
                earthquake currentEarth =mAdapter.getItem(position);
                Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse(currentEarth.getWebLink()));
                startActivity(web);

            });
        }
    }

    @NonNull
    @Override
    public Loader<List<earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i("Idea of Loaders", "onCreateLoader()");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USED_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(EarthquakeActivity.this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<earthquake>> loader, List<earthquake> data) {
        loaderIndicator.setVisibility(View.GONE);
        Log.i("Idea of Loaders", "onLoadFinished()");
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
        else {
            earthQuakeEmpty.setText(R.string.no_earthquake_recently);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<earthquake>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}



