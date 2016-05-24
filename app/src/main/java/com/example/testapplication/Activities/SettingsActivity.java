package com.example.testapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.example.testapplication.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnColor;
    private final static String COLOR_SELECTED = "colorSelected";
    public Intent returnIntent = new Intent();
    private ColorPickerDialog colorPickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        int[] colors = {getResources().getColor(R.color.white), getResources().getColor(R.color.pink), getResources().getColor(R.color.purple),
                getResources().getColor(R.color.deep_purple), getResources().getColor(R.color.indigo), getResources().getColor(R.color.blue),
                getResources().getColor(R.color.light_blue), getResources().getColor(R.color.cyan), getResources().getColor(R.color.teal),
                getResources().getColor(R.color.green), getResources().getColor(R.color.light_green), getResources().getColor(R.color.lime),
                getResources().getColor(R.color.yellow), getResources().getColor(R.color.amber), getResources().getColor(R.color.orange),
                getResources().getColor(R.color.deep_orange)};
        btnColor = (Button) findViewById(R.id.btnColor); // Белый
        btnColor.setOnClickListener(this);
        colorPickerDialog = new ColorPickerDialog();
        colorPickerDialog.initialize(
                R.string.titleColor, colors, 0, 4, 4);
        colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                returnIntent.putExtra(COLOR_SELECTED, color);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnColor:
                colorPickerDialog.show(getFragmentManager(), "1");
                break;
            default:
                break;
        }
    }
}
