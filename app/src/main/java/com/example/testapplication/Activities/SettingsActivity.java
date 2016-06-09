package com.example.testapplication.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.testapplication.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnColor;
    private final static String COLOR_SELECTED = "colorSelected";
    private String titleColor, titleColorOK, titleColorCancel;
    public Intent returnIntent = new Intent();
    ColorPickerDialogBuilder colorPickerDialog;
    private int colorSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnColor = (Button) findViewById(R.id.btnColor);
        btnColor.setOnClickListener(this);
        titleColor = getString(R.string.titleColor);
        titleColorOK = getString(R.string.titleColorOK);
        titleColorCancel = getString(R.string.titleColorCancel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnColor:
                buildColorPicker();
                break;
            default:
                break;
        }
    }

    private void buildColorPicker() {
        colorPickerDialog.with(this)
                .setTitle(titleColor)
                .initialColor(R.color.white)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(7)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        colorSelected = selectedColor;
                    }
                })
                .setPositiveButton(titleColorOK, new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        returnIntent.putExtra(COLOR_SELECTED, colorSelected);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                })
                .setNegativeButton(titleColorCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).build().show();
    }

}
