package com.example.quake;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<earthquake>> {
    private  String url;
    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading() {
        Log.i("Idea of Loaders", "onStartLoading()");
        forceLoad();
    }

    @Nullable
    @Override
    public List<earthquake> loadInBackground() {
        Log.i("Idea of Loaders", "LoadInBackground()");
        return QueryUtils.fetchEarthquakeData(url);
    }

}

