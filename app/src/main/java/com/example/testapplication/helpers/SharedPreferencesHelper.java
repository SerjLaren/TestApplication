package com.example.testapplication.helpers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class SharedPreferencesHelper {

    private final static String SAVED_COLOR = "back_color", SAVE_STOPWATCH = "saveStopwatch", SAVE_TIMER = "saveTimer",
            BOOBS = "boobs", SET_BOOBS = "setBoobs";
    private SharedPreferences pref;
    private SharedPreferences settingsPref;
    private SharedPreferences.Editor edit, editSettingsPref;

    public SharedPreferencesHelper(Activity ctx) {
        pref = ctx.getPreferences(ctx.MODE_PRIVATE);
        settingsPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        edit = pref.edit();
        editSettingsPref = settingsPref.edit();
    }

    public int getSavedColor() {
        return pref.getInt(SAVED_COLOR, Color.WHITE);
    }

    public void putSavedColor(int backColor) {
        edit.putInt(SAVED_COLOR, backColor);
        edit.commit();
    }

    public void putSavedOboobs(String boobsUrl) {
        editSettingsPref.putString(BOOBS, boobsUrl);
        editSettingsPref.commit();
    }

    public String getSavedOboobs() {
        return settingsPref.getString(BOOBS, "");
    }

    public boolean getTimerStoped(String TIMER_STOPED) {
        return pref.getBoolean(TIMER_STOPED, true);
    }

    public void setTimerStopedTRUE(String TIMER_STOPED) {
        edit.putBoolean(TIMER_STOPED, true);
        edit.commit();
    }

    public void setTimerStopedFALSE(String TIMER_STOPED) {
        edit.putBoolean(TIMER_STOPED, false);
        edit.commit();
    }

    public boolean getSaveStopwatch() {
        return settingsPref.getBoolean(SAVE_STOPWATCH, true);
    }

    public boolean getSaveTimer() {
        return settingsPref.getBoolean(SAVE_TIMER, true);
    }

    public boolean getSetBoobsBack() {
        return settingsPref.getBoolean(SET_BOOBS, true);
    }
}
