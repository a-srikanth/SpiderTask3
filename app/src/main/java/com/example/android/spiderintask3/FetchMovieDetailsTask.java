package com.example.android.spiderintask3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Srikanth on 7/8/2016.
 */
public class FetchMovieDetailsTask extends AsyncTask<String, Void, String[]> {

    private String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    Context mContext;

    public FetchMovieDetailsTask(Context context){
        mContext =  context;
        Log.i("FetchMoviesDetailsTask: ","Started");
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
            final String ID_PARAM = "i";
            final String PLOT_PARAM = "plot";

            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                    .appendQueryParameter(ID_PARAM , params[0])
                    .appendQueryParameter(PLOT_PARAM, params[1])
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
                return getMovieDetailsFromJson(receivedJsonStr, params[0]);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return null;
    }


    public String[] getMovieDetailsFromJson(String movieDetailJsonStr, String movieId) throws JSONException{
        final String TITLE = "Title";
        final String YEAR_OF_RELEASE = "Year";
        final String TYPE = "Type";
        final String POSTER_PATH = "Poster";
        final String IMDB_RATING = "imdbRating";
        final String PLOT = "Plot";
        final String GENRE = "Genre";

        JSONObject movieDetails = new JSONObject(movieDetailJsonStr);

        String imdb_id = movieId;
        String title = movieDetails.getString(TITLE);
        String year_of_release = movieDetails.getString(YEAR_OF_RELEASE);
        String type = movieDetails.getString(TYPE);
        String poster_path = movieDetails.getString(POSTER_PATH);
        String imdb_rating = movieDetails.getString(IMDB_RATING);
        String plot = movieDetails.getString(PLOT);
        String genre = movieDetails.getString(GENRE);

        String[] allDetails = {title, year_of_release, genre, imdb_rating, poster_path, plot, type, imdb_id};
        Log.i("Check: ",allDetails[0]);

        return allDetails;

    }

    @Override
    protected void onPostExecute(String[] strings) {
        Intent intent = new Intent(mContext, DetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("allMovieDetails", strings);
        intent.putExtra("fromFavourites",0);
        mContext.startActivity(intent);

        super.onPostExecute(strings);
    }
}
