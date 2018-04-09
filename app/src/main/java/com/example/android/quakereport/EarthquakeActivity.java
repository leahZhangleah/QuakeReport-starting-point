/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<OneEarthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    ArrayList<OneEarthquake> earthquakes = new ArrayList<OneEarthquake>();
    String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    EarthquakeArrayAdapter adapter;
    private static final int EARTHQUAKE_LOADER_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                String url = earthquakes.get(position).getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new EarthquakeArrayAdapter(getApplicationContext(),earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
       // new EarthquakeTask().execute(url);
        getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID,null,this);
    }

    @Override
    public Loader<ArrayList<OneEarthquake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this,url);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<OneEarthquake>> loader, ArrayList<OneEarthquake> oneEarthquakes) {
        adapter.clear();
        if (oneEarthquakes != null && !oneEarthquakes.isEmpty()){
            adapter.addAll(oneEarthquakes);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<OneEarthquake>> loader) {
        adapter.clear();
    }

    /*
    private class EarthquakeTask extends AsyncTask<String, Void, ArrayList<OneEarthquake>>{
        // Create a fake list of earthquake locations.
        @Override
        protected ArrayList<OneEarthquake> doInBackground(String... strings) {
            if (strings.length > 0 || strings[0] != null){
                earthquakes = QueryUtils.fetchEarthquakeData(url);
                return earthquakes;
            }else {
                Log.e(LOG_TAG,"the parameters passed in are null.");
                return null;
            }
        }

        @Override
        protected void onPostExecute(final ArrayList<OneEarthquake> oneEarthquakes) {
            adapter.clear();
            if (oneEarthquakes != null && !oneEarthquakes.isEmpty()){
                adapter.addAll(oneEarthquakes);
            }

        }
    }*/
}
