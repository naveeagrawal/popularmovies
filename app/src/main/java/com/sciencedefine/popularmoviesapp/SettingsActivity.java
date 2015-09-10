package com.sciencedefine.popularmoviesapp;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by naveenagrawal on 10-Sep-15.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
