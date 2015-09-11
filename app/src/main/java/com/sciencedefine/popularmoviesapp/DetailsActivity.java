package com.sciencedefine.popularmoviesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailsActivity extends ActionBarActivity {
    private String[] movieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        //Get the String Array containing movie details from the calling intent
        movieDetails = intent.getStringArrayExtra(Intent.EXTRA_TEXT);
        //Get the handle to Image View
        ImageView moviePosterImageView = (ImageView) findViewById(R.id.poster_image);
        //Get handles to Text Views for updating movie details
        TextView movieTitleTextView = (TextView) findViewById(R.id.movie_title_text);
        TextView moviePlotTextView = (TextView) findViewById(R.id.plot_text);
        TextView movieUserRatingTextView = (TextView) findViewById(R.id.user_rating_text);
        TextView movieReleaseDateTextView = (TextView) findViewById(R.id.release_date_text);
        //Load the movie poster
        new ImageLoadTask(movieDetails[0], moviePosterImageView).execute();
        //Update the text views with movie details
        movieTitleTextView.setText(movieDetails[1]);
        moviePlotTextView.setText(movieDetails[2]);
        movieUserRatingTextView.setText(movieDetails[3]);
        movieReleaseDateTextView.setText(movieDetails[4]);
    }
    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
