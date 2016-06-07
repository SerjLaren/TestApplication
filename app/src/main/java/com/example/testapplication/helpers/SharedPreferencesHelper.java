package com.example.testapplication.helpers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;

public class SharedPreferencesHelper {

    private String SAVED_COLOR = "back_color", COLORS = "colors";
    private SharedPreferences sPref;
    private SharedPreferences.Editor edit;

    public SharedPreferencesHelper(Activity ctx) {
        sPref = ctx.getPreferences(ctx.MODE_PRIVATE);
        edit = sPref.edit();
    }

    public int getSavedColor() {
        return sPref.getInt(SAVED_COLOR, Color.WHITE);
    }

    public void putSavedColor(int backColor) {
        edit.putInt(SAVED_COLOR, backColor);
        edit.commit();

    }


}
