package com.sciencedefine.popularmoviesapp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by naveenagrawal on 10-Sep-15.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
