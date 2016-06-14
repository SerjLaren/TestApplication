package com.example.testapplication.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.testapplication.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);
    }
}
