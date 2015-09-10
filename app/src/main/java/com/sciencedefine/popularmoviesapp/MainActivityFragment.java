package com.sciencedefine.popularmoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    ImageAdapter mAdapter;
    GridView mGridView;
    String[] mImages = new String[20];

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridview);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailsIntent = new Intent(getActivity(), DetailsActivity.class);
                //detailIntent.putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(detailsIntent);
            }
        });
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortPref = sharedPref.getString(getString(R.string.sort_order_key), getString(R.string.default_sort_order));
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute(sortPref);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {
        private String[] getMoviesDataFromJson(String moviesJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_MOVIES = "results";
            final String TMDB_POSTER = "backdrop_path";

            JSONObject tmdbJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = tmdbJson.getJSONArray(TMDB_MOVIES);

            String[] resultStrs = new String[20];
            for(int i = 0; i < 20; i++) {
                // For now, using the poster"
                String poster;

                // Get the JSON object representing the movie
                JSONObject movieJson = moviesArray.getJSONObject(i);

                // poster is the value of the element "backdrop_path" in movieJson Object
                poster = movieJson.getString(TMDB_POSTER);
                //Log.v("Poster Path "+i+":", "http://image.tmdb.org/t/p/w185" + poster);
                resultStrs[i] = "http://image.tmdb.org/t/p/w185" + poster;
            }
            return resultStrs;
        }

        @Override
        protected String[] doInBackground(String... Params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            String apiKey = "854345408413cd9a9d5c8ffb980da45e";
            try {
                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";
                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, Params[0])
                        .appendQueryParameter(API_KEY_PARAM, apiKey)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v("Built URI ", builtUri.toString());
                // Create the request to TheMovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                //Log.v("Json from TMDB", moviesJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attempting
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
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try {
                String[] resultingMovies;
                resultingMovies = getMoviesDataFromJson(moviesJsonStr);
                return resultingMovies;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        /**
         * @param strings resultingMovies String array received from doInBackground
         */
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if(strings != null){
                mImages = strings;
                mAdapter = new ImageAdapter(getActivity(), mImages);
                mGridView.setAdapter(mAdapter);
            }
        }

    }
}
