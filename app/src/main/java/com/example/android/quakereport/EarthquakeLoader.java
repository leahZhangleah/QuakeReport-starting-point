package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<OneEarthquake>> {
    String url = "";

    public EarthquakeLoader(Context context,String url){
        super(context);
        this.url= url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<OneEarthquake> loadInBackground() {
        if (url == null){
            return null;
        }
        ArrayList<OneEarthquake> earthquakes = new ArrayList<>();
        earthquakes = QueryUtils.fetchEarthquakeData(url);
        return earthquakes;

    }
}
