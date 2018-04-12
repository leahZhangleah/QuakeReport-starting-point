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
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<OneEarthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    ArrayList<OneEarthquake> earthquakes = new ArrayList<OneEarthquake>();
    String url = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    EarthquakeArrayAdapter adapter;
    private static final int EARTHQUAKE_LOADER_ID = 0;
    TextView emptyView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG,LOG_TAG + "is being created now");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake);

        //empty text view
        emptyView = (TextView) findViewById(R.id.empty_view);
        //progress bar to show before data is loading
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setEmptyView(emptyView);
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
        //check if there is internet connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (isConnected){
            // new EarthquakeTask().execute(url);
            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID,null,this);
            Log.i(LOG_TAG,"I am initLoader method");
        }else {
            progressBar.setVisibility(View.GONE);
            emptyView.setText("No internet connection");
        }

    }

    @Override
    public Loader<ArrayList<OneEarthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG,"I am oncreateloader method");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String minMagnitude = sharedPreferences.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));
        String order_by = sharedPreferences.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_default));
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(url);
        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();
        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit","10");
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("orderby",order_by);

        // Return the completed uri `http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=10&minmag=minMagnitude&orderby=time
        return new EarthquakeLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<OneEarthquake>> loader, ArrayList<OneEarthquake> oneEarthquakes) {
        adapter.clear();
        progressBar.setVisibility(View.GONE);
        Log.i(LOG_TAG,"adapter's data is cleared");
        if (oneEarthquakes != null && !oneEarthquakes.isEmpty()){
            Log.i(LOG_TAG,"I am onloadfinished method");
            adapter.addAll(oneEarthquakes);
        }else{
            emptyView.setText("No earthquakes found.");
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<OneEarthquake>> loader) {
        adapter.clear();
        Log.i(LOG_TAG,"adapter's data is reset");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_seting){
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
