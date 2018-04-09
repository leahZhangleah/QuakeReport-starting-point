package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<OneEarthquake>> {
    private static final String LOG_TAG = EarthquakeLoader.class.getName();
    String url = "";

    public EarthquakeLoader(Context context,String url){
        super(context);
        this.url= url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"I am starting loader");
        forceLoad();
    }

    @Override
    public ArrayList<OneEarthquake> loadInBackground() {
        if (url == null){
            return null;
        }
        Log.i(LOG_TAG,"I am loading data on background");
        ArrayList<OneEarthquake> earthquakes = new ArrayList<>();
        earthquakes = QueryUtils.fetchEarthquakeData(url);
        return earthquakes;

    }
}
