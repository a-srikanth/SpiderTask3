package com.example.android.spiderintask3;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Srikanth on 7/7/2016.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

    private String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    Context mContext;
    private GridView mGridView;
    private ArrayList<GridItem> mGridData;
    private GridViewAdapter mGridAdapter;

    //String arrays to store the information of movies received
    //until it is added to the database.
    String[] titles,poster_paths,imdb_id,year_of_release,type;

    public FetchMoviesTask(Activity activity , Context context) {
        mContext = context;
        mGridView = (GridView) activity.findViewById(R.id.search_result_grid);

        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(activity,mContext,R.layout.grid_item, mGridData);
        mGridAdapter.setGridData(mGridData);

        mGridView.setAdapter(mGridAdapter);
        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);
                new FetchMovieDetailsTask(mContext).execute(imdb_id[position],"full");
            }
        });
    }

    @Override
    protected String[] doInBackground(String... params) {

        //Check whether the params has any value or not
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String receivedJsonStr = null;

        try {

            final String MOVIEDB_BASE_URL = "http://www.omdbapi.com/?";
            final String SEARCH_PARAM = "s";
            final String PAGE_PARAM = "page";

            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                    .appendQueryParameter(SEARCH_PARAM, params[0])
                    .appendQueryParameter(PAGE_PARAM, params[1])
                    .build();
            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Toast.makeText(mContext, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if the completed buffer is printed
                // out for debugging
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                Toast.makeText(mContext, "Please try again", Toast.LENGTH_SHORT).show();
                // Stream was empty.  No point in parsing.
                return null;
            }
            receivedJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
                return getMoviesDataFromJson(receivedJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return null;
    }

    public String[] getMoviesDataFromJson(String moviesJsonStr) throws JSONException{
        final String SEARCH_RESULTS = "Search";
        final String TITLE = "Title";
        final String YEAR_OF_RELEASE = "Year";
        final String TYPE = "Type";
        final String POSTER_PATH = "Poster";
        final String IMDB_ID = "imdbID";

        JSONObject movieListJSON = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = movieListJSON.getJSONArray(SEARCH_RESULTS);

        titles = new String[resultsArray.length()];
        year_of_release = new String[resultsArray.length()];
        imdb_id = new String[resultsArray.length()];
        type = new String[resultsArray.length()];
        poster_paths = new String[resultsArray.length()];

        if(mGridData!=null)
            mGridData.clear();

        for(int i=0 ; i<resultsArray.length() ; i++){
            GridItem item = new GridItem();

            JSONObject movieDetails = resultsArray.getJSONObject(i);

            titles[i] = movieDetails.getString(TITLE);
            year_of_release[i] = movieDetails.getString(YEAR_OF_RELEASE);
            type[i] = movieDetails.getString(TYPE);
            poster_paths[i] = movieDetails.getString(POSTER_PATH);
            imdb_id[i] = movieDetails.getString(IMDB_ID);

            item.setImagePath(poster_paths[i]);

            mGridData.add(item);
        }
        return poster_paths;
    }

    @Override
    protected void onPostExecute(String[] strings) {
        if (strings != null) {
            mGridAdapter.setGridData(mGridData);
            Log.i("onPostExecute ", "Added Grid data");
        } else {
            Toast.makeText(mContext, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(strings);
    }
}