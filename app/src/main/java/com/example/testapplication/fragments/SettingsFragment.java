package com.example.testapplication.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.testapplication.R;

/**
 * Created by Sergey on 13.06.2016.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);
    }
}
